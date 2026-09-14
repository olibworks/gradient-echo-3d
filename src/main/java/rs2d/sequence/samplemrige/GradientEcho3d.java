package rs2d.sequence.samplemrige;
// ---------------------------------------------------------------------
//
//                 SampleMriGe PSD
//
// ---------------------------------------------------------------------
//
// 17/11/2017   V2.2
//      - TX_LENGTH   TX_SHAPE
// Version 2.1 : 31/10/2017
// Version 2 : 30/10/2017


import rs2d.commons.log.Log;
import rs2d.sequence.common.RFPulse;
import rs2d.spinlab.api.Hardware;
import rs2d.spinlab.api.PowerComputation;
import rs2d.spinlab.data.transformPlugin.TransformPlugin;
import rs2d.spinlab.exception.ConfigurationException;
import rs2d.spinlab.instrument.util.GradientMath;
import rs2d.spinlab.pulse.PulseType;
import rs2d.spinlab.sequence.table.Shape;
import rs2d.spinlab.sequence.table.Table;
import rs2d.spinlab.sequence.table.Utility;
import rs2d.spinlab.sequenceGenerator.BaseSequenceGenerator;
import rs2d.spinlab.sequenceGenerator.util.GradientRotation;
import rs2d.spinlab.sequenceGenerator.util.TimeEvents;
import rs2d.spinlab.tools.param.NumberParam;
import rs2d.spinlab.tools.param.TextParam;
import rs2d.spinlab.tools.param.UnitEnum;
import rs2d.spinlab.tools.table.Order;
import rs2d.spinlab.tools.utility.MathUtility;
import rs2d.spinlab.tools.utility.Nucleus;

import java.util.ArrayList;
import java.util.List;
import static java.util.Arrays.asList;

import static rs2d.sequence.samplemrige.S.*;


import static rs2d.sequence.samplemrige.U.*;




// **************************************************************************************************
// *************************************** SEQUENCE GENERATOR ***************************************
// **************************************************************************************************
public class GradientEcho3d extends BaseSequenceGenerator {
    private static final double MIN_INSTRUCTION_DELAY = 0.000005;     // single instruction minimal duration

    private double observeFrequency;
    private double gMax;
    private Nucleus nucleus;

    private int acquisitionMatrixDimension1D;
    private int acquisitionMatrixDimension2D;
    private int acquisitionMatrixDimension3D;
    private int acquisitionMatrixDimension4D;
    private int userMatrixDimension1D;
    private int userMatrixDimension2D;
    private int userMatrixDimension3D;
    private int userMatrixDimension4D;

    private int nb_scan_2d;
    private int nb_scan_3d;
    private int nb_scan_4d;

    private int manualAtt;

    private double spectralWidth;
    private double tr;
    private double te;

    private double fov3d;
    private double fov;
    private double fovPhase;
    private double off_center_distance_1D;
    private double off_center_distance_2D;
    private double off_center_distance_3D;

    private double txLength90;

    private boolean isEnablePhase;
    private boolean isEnablePhase3D;
    private boolean isEnableSlice;
    private boolean isEnableRead;

    private boolean autoRF;

    private double observation_time;


    public GradientEcho3d() {
        addUserParams();
    }

    @Override
    public void init() {
        super.init();

        // Define default, min, max and suggested values regarding the instrument
        getParam(MAGNETIC_FIELD_STRENGTH).setDefaultValue(Hardware.getMagnetFieldStrength());
        getParam(DIGITAL_FILTER_SHIFT).setDefaultValue(Hardware.getNbAcquisitionDeadPoints());
        getParam(DIGITAL_FILTER_REMOVED).setDefaultValue(Hardware.isRemoveAcquisitionDeadPoints());

        TextParam txShape = getParam(TX_SHAPE);
        txShape.setSuggestedValues(List.of("GAUSSIAN"));
        txShape.setRestrictedToSuggested(true);

        // Restrict Lists
        List<String> tx_shape = asList(
                "HARD",
                "GAUSSIAN",
                "SINC3",
                "SINC5",
                "SLR_8_5152",
                "SLR_4_2576");
        ((TextParam) getParam(TX_SHAPE)).setSuggestedValues(tx_shape);
        ((TextParam) getParam(TX_SHAPE)).setRestrictedToSuggested(true);

        //TRANSFORM PLUGIN
        TextParam transformPlugin = getParam(TRANSFORM_PLUGIN);
        transformPlugin.setSuggestedValues(List.of("Sequential4D"));
        transformPlugin.setRestrictedToSuggested(true);

    }

    // ==============================
    // -----   GENERATE
    // ==============================
    @Override
    public void generate() throws Exception {
        initUserParam();
        this.beforeRouting();
        if (!this.isRouted()) {
            this.route();
        }
        this.afterRouting();    //avoid exception during setup

        this.checkAndFireException();
    }

    private void initUserParam() {
        acquisitionMatrixDimension2D = getInt(ACQUISITION_MATRIX_DIMENSION_2D);
        acquisitionMatrixDimension3D = getInt(ACQUISITION_MATRIX_DIMENSION_3D);
        acquisitionMatrixDimension4D = getInt(ACQUISITION_MATRIX_DIMENSION_4D);

        userMatrixDimension1D = getInt(USER_MATRIX_DIMENSION_1D);
        userMatrixDimension2D = getInt(USER_MATRIX_DIMENSION_2D);
        userMatrixDimension3D = getInt(USER_MATRIX_DIMENSION_3D);
        userMatrixDimension4D = getInt(USER_MATRIX_DIMENSION_4D);

        spectralWidth = getDouble(SPECTRAL_WIDTH, UnitEnum.Hertz); // get user defined spectral width in Hz
        tr = getDouble(REPETITION_TIME);
        te = getDouble(ECHO_TIME);

        fov3d = getDouble(FIELD_OF_VIEW_3D);
        fov = getDouble(FIELD_OF_VIEW);
        fovPhase = getDouble(FIELD_OF_VIEW_PHASE);
        off_center_distance_1D = getDouble(OFF_CENTER_FIELD_OF_VIEW_1D);
        off_center_distance_2D = getDouble(OFF_CENTER_FIELD_OF_VIEW_2D);
        off_center_distance_3D = getDouble(OFF_CENTER_FIELD_OF_VIEW_3D);

        txLength90 = getDouble(TX_LENGTH);

        isEnablePhase = getBoolean(GRADIENT_ENABLE_PHASE);
        isEnablePhase3D = getBoolean(GRADIENT_ENABLE_PHASE_3D);
        isEnableSlice = getBoolean(GRADIENT_ENABLE_SLICE);
        isEnableRead = getBoolean(GRADIENT_ENABLE_READ);

        observation_time = getDouble(ACQUISITION_TIME_PER_SCAN);
    }

    // --------------------------------------------------------------------------------------------------------------------------------------------
    // -- BEFORE ROUTING --- BEFORE ROUTING --- BEFORE ROUTING --- BEFORE ROUTING --- BEFORE ROUTING --- BEFORE ROUTING --- BEFORE ROUTING ---
    // --------------------------------------------------------------------------------------------------------------------------------------------
    //
    //                                                          BEFORE ROUTING
    //
    // --------------------------------------------------------------------------------------------------------------------------------------------
    private void beforeRouting() throws Exception {
        Log.debug(getClass(), "------------ BEFORE ROUTING -------------");

        getParam(SEQUENCE_VERSION).setValue(getVersion());
        getParam(MODALITY).setValue("MRI");
        // -----------------------------------------------
        // RX parameters : nucleus, RX gain & frequencies
        // -----------------------------------------------
        nucleus = Nucleus.getNucleusForName((String) getParam(NUCLEUS_1).getValue());
        double protonFrequency = Hardware.getProtonFrequency();
        double freq_offset1 = getDouble(OFFSET_FREQ_1, UnitEnum.Hertz);
        observeFrequency = nucleus.getFrequency(protonFrequency) + freq_offset1;
        getParam(BASE_FREQ_1).setValue(nucleus.getFrequency(protonFrequency));

        gMax = Math.abs(GradientMath.getMaxGradientStrength());

        set(Rx_gain, RECEIVER_GAIN);
        getParam(RECEIVER_COUNT).setValue(Hardware.getReceiverCount(nucleus));

        set(Intermediate_frequency, Hardware.getIntermediateFrequency());
        getParam(INTERMEDIATE_FREQUENCY).setValue(Hardware.getIntermediateFrequency());

        set(Tx_frequency, observeFrequency);
        getParam(OBSERVED_FREQUENCY).setValue(observeFrequency);

        set(Tx_nucleus, NUCLEUS_1);
        getParam(OBSERVED_NUCLEUS).setValue(nucleus);


        // -----------------------------------------------
        // 1stD managment     FSE
        // -----------------------------------------------
        // MATRIX
        acquisitionMatrixDimension1D = userMatrixDimension1D;
        spectralWidth = Hardware.getNearestSpectralWidth(spectralWidth); // get real spectral width from Cameleon
        setNumber(SPECTRAL_WIDTH, spectralWidth, UnitEnum.Hertz); // display spectral width, ensure it is set in Hz

        observation_time = acquisitionMatrixDimension1D / spectralWidth;
        getParam(ACQUISITION_TIME_PER_SCAN).setValue(observation_time);   // display observation pulseDuration

        // -----------------------------------------------
        // 2nd D managment
        // -----------------------------------------------
        // FOV
        double fov_phase = getDouble(FIELD_OF_VIEW_PHASE);
        fov_phase = fov_phase > fov ? fov_phase : fov;
        getParam(FIELD_OF_VIEW_PHASE).setValue(fov_phase);
        // MATRIX
        acquisitionMatrixDimension2D = (int) Math.floor(Math.round(userMatrixDimension2D) / 2.0) * 2;
        acquisitionMatrixDimension2D = (acquisitionMatrixDimension2D < 4) && isEnablePhase ? 2 : acquisitionMatrixDimension2D;

        nb_scan_2d = acquisitionMatrixDimension2D;

        // -----------------------------------------------
        // 3D managment 1/2: matrix & scan
        // ------------------------------------------------
        // MATRIX

        //Calculate the number of k-space lines acquired in the 3rd Dimension : acquisitionMatrixDimension3D
        
        acquisitionMatrixDimension3D = (int) Math.floor(Math.round(userMatrixDimension3D) / 2.0) * 2;
        acquisitionMatrixDimension3D = (acquisitionMatrixDimension3D < 4) && isEnablePhase3D ? 2 : acquisitionMatrixDimension3D;
        nb_scan_3d = acquisitionMatrixDimension3D;
        getParam(NUMBER_OF_SHOOT_3D).setValue(nb_scan_3d);
        //Calculate Resulotion for Display
        getParam(RESOLUTION_3D).setValue(fov3d/acquisitionMatrixDimension3D);

        // Pixel dimension calculation
        // -----------------------------------------------
        // 4D managment:  Dynamic, MultiEcho, External triggering
        // -----------------------------------------------
        acquisitionMatrixDimension4D = userMatrixDimension4D;
        nb_scan_4d = userMatrixDimension4D;

        getParam(TRANSFORM_PLUGIN).setValue("Sequential4D");

        // -----------------------------------------------
        // set the ACQUISITION_MATRIX and Nb XD
        // -----------------------------------------------        // set the calculated acquisition matrix
        getParam(ACQUISITION_MATRIX_DIMENSION_1D).setValue(acquisitionMatrixDimension1D);
        getParam(ACQUISITION_MATRIX_DIMENSION_2D).setValue(acquisitionMatrixDimension2D);
        getParam(ACQUISITION_MATRIX_DIMENSION_3D).setValue(acquisitionMatrixDimension3D);
        getParam(ACQUISITION_MATRIX_DIMENSION_4D).setValue(acquisitionMatrixDimension4D);

        // set the calculated sequence dimensions
        
        set(Pre_scan, DUMMY_SCAN); // Do the prescan
        set(Nb_point, acquisitionMatrixDimension1D);
        set(Nb_1d, NUMBER_OF_AVERAGES);
        set(Nb_2d, nb_scan_2d);
        set(Nb_3d, nb_scan_3d);
        set(Nb_4d, nb_scan_4d);

        // -----------------------------------------------
        // Image Orientation
        // -----------------------------------------------
        //Offset according to READ PHASE and SLICE
        off_center_distance_1D = getOff_center_distance_1D_2D_3D(1);
        off_center_distance_2D = getOff_center_distance_1D_2D_3D(2);
        off_center_distance_3D = getOff_center_distance_1D_2D_3D(3);

        //Offset according to ENABLE READ PHASE and SLICE
        off_center_distance_1D = isEnableRead ? off_center_distance_1D : 0;
        off_center_distance_2D = isEnablePhase ? off_center_distance_2D : 0;
        off_center_distance_3D = 0;

		// just 2d off center correction not 3d


        // MEMORY LIMITATION 2D Shift
        getParam(OFF_CENTER_FIELD_OF_VIEW_3D).setValue(off_center_distance_3D);
        getParam(OFF_CENTER_FIELD_OF_VIEW_2D).setValue(off_center_distance_2D);
        getParam(OFF_CENTER_FIELD_OF_VIEW_1D).setValue(off_center_distance_1D);

        // -----------------------------------------------
        // activate gradient rotation matrix
        // -----------------------------------------------
        GradientRotation.setSequenceGradientRotation(this);
    }

    // --------------------------------------------------------------------------------------------------------------------------------------------
    // -- AFTER ROUTING --- AFTER ROUTING --- AFTER ROUTING --- AFTER ROUTING --- AFTER ROUTING --- AFTER ROUTING --- AFTER ROUTING ---  AFTER ROUTING ---
    // --------------------------------------------------------------------------------------------------------------------------------------------
    //
    //                                                          AFTER ROUTING
    //
    // --------------------------------------------------------------------------------------------------------------------------------------------
    private void afterRouting() throws Exception {
        TransformPlugin plugin = getTransformPlugin();
        plugin.setParameters(new ArrayList<>(getUserParams()));

        // -----------------------------------------------
        // enable gradient lines
        // -----------------------------------------------
        set(Grad_enable_read, GRADIENT_ENABLE_READ);              // pass gradient line status to sequence
        set(Grad_enable_phase_2D, isEnablePhase);
        set(Grad_enable_phase_3D, isEnablePhase3D);
        set(Grad_enable_slice, false);

        boolean is_grad_spoiler = getBoolean(GRADIENT_ENABLE_SPOILER);// get slice refocussing ratio
        set(Grad_enable_spoiler_slice, is_grad_spoiler);
        set(Grad_enable_spoiler_phase, is_grad_spoiler);
        set(Grad_enable_spoiler_read, is_grad_spoiler);

        // -----------------------------------------------
        // calculate gradient equivalent rise time
        // -----------------------------------------------
        double grad_rise_time = getDouble(GRADIENT_RISE_TIME);
        double min_rise_time_sinus = GradientMath.getShortestRiseTime(100.0) * Math.PI / 2;
        if (grad_rise_time < min_rise_time_sinus) {
            double new_grad_rise_time = ceilToSubDecimal(min_rise_time_sinus, 5);
            NumberParam gradientRiseTime = getParam(GRADIENT_RISE_TIME);
            notifyOutOfRangeParam(GRADIENT_RISE_TIME, new_grad_rise_time, gradientRiseTime.getMaxValue(), "Gradient ramp pulseDuration too short");
            grad_rise_time = new_grad_rise_time;
        }
        set(Time_grad_ramp, grad_rise_time);

        double grad_shape_rise_factor_up = Utility.voltageFillingFactor(getSequenceTable(Grad_shape_up));
        double grad_shape_rise_factor_down = Utility.voltageFillingFactor(getSequenceTable(Grad_shape_down));
        double grad_shape_rise_time = grad_shape_rise_factor_up * grad_rise_time + grad_shape_rise_factor_down * grad_rise_time;        // shape dependant equivalent rise time

        // ----------- init variables---------------------
        set(Frequency_offset_init, 0);// PSD should start with a zero offset frequency pulse

        // -----------------------------------------------
        // Calculation RF pulse parameters  1/3 : Shape
        // -----------------------------------------------
        double flipAngle = getDouble(FLIP_ANGLE);
		
		set(Time_tx, txLength90);
		
		RFPulse pulseTX = RFPulse.createRFPulse(
		        getSequence(),
		        Tx_att,
		        Tx_amp,
		        Tx_phase,
		        Time_tx,
		        Tx_shape,
		        Tx_shape_phase,
		        Tx_freq_offset,
		        nucleus
		);
		
		pulseTX.setShape(getText(TX_SHAPE), 128, "Hamming");

		boolean autoRF=getBoolean(TX_AMP_ATT_AUTO);

		if (autoRF) {
		
			if (!pulseTX.prepPowerWithFlipAngle(observeFrequency, flipAngle)) {
			    txLength90 = pulseTX.getPulseDuration();
			
			    notifyOutOfRangeParam(
			            TX_LENGTH,
			            txLength90,
			            ((NumberParam) getParam(TX_LENGTH)).getMaxValue(),
			            "Pulse length too short for the selected RF shape and flip angle"
			    );
			
			    getParam(TX_LENGTH).setValue(txLength90);
			    set(Time_tx, txLength90);
			}

			//Choose attenuation such that this pule uses about 80% of output range
			
			pulseTX.prepChannelAttWithPower(
			        getListInt(TX_ROUTE),
			        80,
			        pulseTX.getPower()
			);
			
			pulseTX.prepTxAmp(getListInt(TX_ROUTE));
			
			getParam(TX_ATT).setValue(pulseTX.getAtt());
			getParam(TX_AMP_90).setValue(pulseTX.getAmp());
			
			set(Tx_att, pulseTX.getAtt());
			set(Tx_amp, pulseTX.getAmp());
		} else {

			// Manual attenuation and amplitude; limit amplitude to safe hardware output.
		    int manualAtt = getInt(TX_ATT);
		    pulseTX.prepChannelAtt(manualAtt);
		
		    double maxAmp = pulseTX.getAmpLimit(
		            observeFrequency,
		            getListInt(TX_ROUTE)
		    );
		
		    double manualAmp = Math.min(getDouble(TX_AMP_90), maxAmp);
		
		    if (manualAmp != getDouble(TX_AMP_90)) {
		        notifyOutOfRangeParam(
		                TX_AMP_90,
		                manualAmp,
		                ((NumberParam) getParam(TX_AMP_90)).getMaxValue(),
		                "RF amplitude too high for the selected coil and attenuation"
		        );
		    }

		    pulseTX.setAmp(manualAmp);
		
		    set(Tx_att, manualAtt);
		    set(Tx_amp, manualAmp);
		}

	
        // -----------------------------------------------
        // Calculation RF pulse parameters  3/3: bandwidth
        // -----------------------------------------------
        //double tx_bandwidth_factor_90 = 1.35; // for gaussian pulse
        //double tx_bandwidth_90 = tx_bandwidth_factor_90 / txLength90;

        double tx_bandwidth_factor_90 = getTx_bandwidth_factor(TX_SHAPE, TX_BANDWIDTH_FACTOR_3D);
        double tx_bandwidth_90 = tx_bandwidth_factor_90 / txLength90;


        //Non-selective excitation: the former slice axis is the 3D partition axis.
        set(Grad_amp_slice,0);

        // -----------------------------------------------
        // calculate ADC observation time
        // -----------------------------------------------
        set(Time_rx, observation_time);

        // -----------------------------------------------
        // calculate READ gradient amplitude
        // -----------------------------------------------
        double grad_amp_read_read;
        grad_amp_read_read = spectralWidth / ((GradientMath.GAMMA / nucleus.getRatio()) * fov);                 // amplitude in T/m

        if (Math.abs(grad_amp_read_read) > gMax) {
            double spectral_width_max = gMax * ((GradientMath.GAMMA / nucleus.getRatio()) * fov);
            double SWmax = 78125000 / 16;
            spectral_width_max = (SWmax * 8) / ((Math.floor(SWmax * 8 / spectral_width_max) + 1));
            getUnreachParamExceptionManager().addParam(SPECTRAL_WIDTH.name(), spectralWidth, ((NumberParam) getParam(SPECTRAL_WIDTH)).getMinValue(),spectral_width_max, "SPECTRAL_WIDTH too high for the readout gradient");
            spectralWidth = spectral_width_max;
            grad_amp_read_read = spectralWidth / ((GradientMath.GAMMA / nucleus.getRatio()) * fov);
        }
        grad_amp_read_read = grad_amp_read_read * 100.0 / gMax;
        set(Grad_amp_read_read, grad_amp_read_read);
        set(Spectral_width, this.spectralWidth);

        // -------------------------------------------------------------------------------------------------
        // pre-calculate SLICE_REF/3D, PHASE_2D,and READ_PREP max area
        // -------------------------------------------------------------------------------------------------
        double grad_phase_application_time = getDouble(GRADIENT_PHASE_APPLICATION_TIME);

        // The 3D axis is phase encoded; it has no slice-selection or rephasing area.
        double grad_total_area_phase_3D = ((acquisitionMatrixDimension3D - 1)
                / ((GradientMath.GAMMA / nucleus.getRatio()) * fov3d)) * 100.0 / gMax;
        double grad_index_max_phase_3D = 0.5; //symmetric k-space around zero
        double grad_max_area_phase_3D = grad_index_max_phase_3D * grad_total_area_phase_3D;

        // pre-calculate PHASE 2D ENCODING max area
        double grad_ratio_read_prep = getDouble(PREPHASING_READ_GRADIENT_RATIO);      // get prephasing gradient ratio
        double grad_total_area_phase_2D; // Total gradient area to be played: from -N/2 to N/2
        double grad_index_max_phase_2D;  // first k'space encoding in percent of grad_total_area_phase_2D
        double grad_max_area_phase_2D;   // Max gradient area to be played
        grad_total_area_phase_2D = ((acquisitionMatrixDimension2D - 1) / ((GradientMath.GAMMA / nucleus.getRatio()) * fovPhase)) * 100.0 / gMax;
        grad_index_max_phase_2D = 1 / 2.0;// symetric k'space around zero
        grad_max_area_phase_2D = grad_index_max_phase_2D * grad_total_area_phase_2D;

        // pre-calculate READ_prephasing max area
        double grad_area_read_prep, grad_area_read_read;
        grad_area_read_read = (observation_time + grad_shape_rise_time) * grad_amp_read_read;   // area of read gradient
        grad_area_read_prep = grad_area_read_read * grad_ratio_read_prep;                       // area of prephasing read gradient

        // Check whether both phase encoders and the read prephaser fit in the block.
        double grad_area_sequence_max = 100 * (grad_phase_application_time + grad_shape_rise_time);
        double grad_area_max = Math.max(grad_max_area_phase_3D, Math.max(grad_area_read_prep, grad_max_area_phase_2D));            // calculate the maximum gradient aera between SLICE REFOC & READ PREPHASING
        if (grad_area_max > grad_area_sequence_max) {
            double grad_phase_application_time_min = grad_area_max / 100.0 - grad_shape_rise_time;
            grad_phase_application_time_min = ceilToSubDecimal(grad_phase_application_time_min, 5);
            notifyOutOfRangeParam(GRADIENT_PHASE_APPLICATION_TIME, grad_phase_application_time_min, ((NumberParam) getParam(GRADIENT_PHASE_APPLICATION_TIME)).getMaxValue(), "Gradient application time too short to reach this pixel dimension");
            grad_phase_application_time = grad_phase_application_time_min;
        }
        set(Time_grad_phase_top, grad_phase_application_time);
        // ------------------------------------------
        // Calculate SLICE_REF/3D, PHASE_2D,and READ_PREP Grad Amplitude with correct application_time
        // ------------------------------------------

        // 3D partition encoding
        double grad_total_amp_phase_3D = grad_total_area_phase_3D / (grad_phase_application_time + grad_shape_rise_time);
        setTableOrder(Grad_amp_phase_3D, Order.Three);
        generateTable(Grad_amp_phase_3D, acquisitionMatrixDimension3D,
                i -> -(grad_index_max_phase_3D * grad_total_amp_phase_3D)
                        + i * grad_total_amp_phase_3D / (acquisitionMatrixDimension3D - 1));

        // calculate PHASE 2D ENCODING Gradient
        double grad_total_amp_phase_2D = grad_total_area_phase_2D / (grad_phase_application_time + grad_shape_rise_time);
        setTableOrder(Grad_amp_phase_2D, Order.Two);
        generateTable(Grad_amp_phase_2D, acquisitionMatrixDimension2D,
                i -> -(grad_index_max_phase_2D * grad_total_amp_phase_2D) + i * grad_total_amp_phase_2D / (acquisitionMatrixDimension2D - 1)
        );
        double rfSpoilingIncrement = getDouble(RF_SPOILING_INCREMENT);

        setTableOrder(Tx_phase, Order.Two);
        generateTable(Tx_phase, acquisitionMatrixDimension2D,
                i -> MathUtility.positiveAngle(
                        rfSpoilingIncrement * i * (i + 1) / 2.0));

        // calculate READ_prephasing Gradient
        double grad_amp_read_prep = grad_area_read_prep / (grad_phase_application_time + grad_shape_rise_time);
        set(Grad_amp_read_prep, -grad_amp_read_prep);

        // --------------------------------------------------------------------------------------------------------------------------------------------
        // TIMING --- TIMING --- TIMING --- TIMING --- TIMING --- TIMING --- TIMING --- TIMING --- TIMING --- TIMING --- TIMING --- TIMING --- TIMING
        // --------------------------------------------------------------------------------------------------------------------------------------------
        //
        //                                                          index of sequence events
        //
        // --------------------------------------------------------------------------------------------------------------------------------------------
        int eventStart = 0;
        int event90 = 3;
        int eventDelay1 = 8;
        int eventAcq = 10;
        int eventDelay2 = 12;
        int eventEnd = 16;

        set(Time_min_instruction, MIN_INSTRUCTION_DELAY);

        // ------------------------------------------
        // calculate delays adapted to current TE & search for incoherence
        // ------------------------------------------
        // calculate actual delays between Rf-pulses and ADC
        double time1 = TimeEvents.getTimeBetweenEvents(getSequence(), event90 + 1, eventAcq - 1);
        time1 = time1 + txLength90 / 2 + observation_time / 2;// Actual_TE
        time1 -= TimeEvents.getTimeForEvents(getSequence(), eventDelay1); // Actual_TE without delay1

        // get minimal TE value & search for incoherence
        double max_time = ceilToSubDecimal(time1, 5);
        double te_min = max_time + MIN_INSTRUCTION_DELAY;
        if (te < te_min) {
            te_min = ceilToSubDecimal(te_min, 5);
            notifyOutOfRangeParam(ECHO_TIME, te_min, ((NumberParam) getParam(ECHO_TIME)).getMaxValue(), "TE too short for the User Mx1D and SW");
            te = te_min;//
        }

        // set calculated the time delays to get the proper TE
        double delay1 = te - time1;
        set(Time_te_delay1, delay1);

        // ------------------------------------------
        // delays for FIR
        // ------------------------------------------
        boolean is_FIR = Hardware.isRemoveAcquisitionDeadPoints();
        double lo_FIR_dead_point = is_FIR ? Hardware.getNbAcquisitionDeadPoints() : 0;
        double min_FIR_delay = (lo_FIR_dead_point + 2) / spectralWidth;
        double min_FIR_4pts_delay = 4 / spectralWidth;

        double time2 = TimeEvents.getTimeBetweenEvents(getSequence(), eventAcq + 1, eventDelay2 - 1);
        set(Time_FIR_delay, Math.max(min_FIR_4pts_delay - time2, MIN_INSTRUCTION_DELAY));

        // -------------------------------------------------------------------------------------------------
        // calculate Phase 2D, 3D and Read SPOILER area, check Grad_Spoil < GMAX
        // -------------------------------------------------------------------------------------------------
        List<Double> grad_amp_spoiler_sl_ph_re = getListDouble(GRAD_AMP_SPOILER_SL_PH_RE);

        // calculate Slice Spoiler Grad Amplitude
        double grad_amp_spoiler_sl = grad_amp_spoiler_sl_ph_re.get(0);  //%
        double grad_amp_spoiler_ph = grad_amp_spoiler_sl_ph_re.get(0);  //%
        double grad_amp_spoiler_re = grad_amp_spoiler_sl_ph_re.get(2);  //%

        set(Grad_amp_spoiler_read, grad_amp_spoiler_re);
        set(Grad_amp_spoiler_slice, grad_amp_spoiler_sl);
        set(Grad_amp_spoiler_phase, grad_amp_spoiler_ph);
        // ---------------------------------------------------------------
        // calculate TR , Time_last_delay  Time_TR_delay & search for incoherence
        // ---------------------------------------------------------------

        double time_seq_to_end_spoiler = TimeEvents.getTimeBetweenEvents(getSequence(), eventStart, eventEnd - 1);
        double tr_min = time_seq_to_end_spoiler + MIN_INSTRUCTION_DELAY;
        double tr_min_rx_FIR = min_FIR_delay + observation_time; // a delay of at least min_FIR_delay must exist between 2 consecutive RX
        tr_min = Math.max(tr_min, tr_min_rx_FIR);

        if (tr < tr_min) {
            tr_min = ceilToSubDecimal(tr_min, 3);
            notifyOutOfRangeParam(REPETITION_TIME, tr_min, ((NumberParam) getParam(REPETITION_TIME)).getMaxValue(), "TR too short to reach (ETL * User Mx3D/Shoot3D) in a single scan");
            tr = tr_min;
        }
        // ------------------------------------------
        // set calculated TR
        // ------------------------------------------
        // set  TR delay to compensate and trigger delays
        double tr_delay = tr - time_seq_to_end_spoiler;

        set(Time_tr_delay, tr_delay);

        // ------------------------------------------------------------------
        // Total Acquisition Time
        // ------------------------------------------------------------------
        int number_of_averages = getInt(NUMBER_OF_AVERAGES);
        int dummyScans = getInt(DUMMY_SCAN);
        double imagingTime = number_of_averages * nb_scan_3d * nb_scan_2d *nb_scan_4d* tr;
        double total_acquisition_time = imagingTime + dummyScans * tr;
        getParam(SEQUENCE_TIME).setValue(total_acquisition_time);

        // Non-selective RF has no slice-position frequency compensation.
        set(Tx_freq_offset, 0);
        set(Freq_offset_tx_prep, 0);
        set(Freq_offset_tx_comp, 0);

        //----------------------------------------------------------------------
        // OFF CENTER FIELD OF VIEW 1D
        // modify RX FREQUENCY OFFSET
        //----------------------------------------------------------------------
        double grad_amp_read_read_mTpm = spectralWidth / ((GradientMath.GAMMA / nucleus.getRatio()) * fov);// amplitude in T/m
        double frequency_offset_1d = -grad_amp_read_read_mTpm * off_center_distance_1D * (GradientMath.GAMMA / nucleus.getRatio());
        set(Rx_freq_offset, frequency_offset_1d);


        //----------------------------------------------------------------------
        // modify RX FREQUENCY Prep and comp
        //----------------------------------------------------------------------
        double timeADC1 = TimeEvents.getTimeBetweenEvents(getSequence(), eventAcq - 1, eventAcq - 1) + observation_time / 2.0;
        double timeADC2 = TimeEvents.getTimeBetweenEvents(getSequence(), eventAcq + 1, eventAcq + 2) + observation_time / 2.0;
        set(FreqOffset_rx_prep, -((frequency_offset_1d * timeADC1) % 1) / grad_phase_application_time);
        set(FreqOffset_rx_comp, -((frequency_offset_1d * timeADC2) % 1) / grad_phase_application_time);

        // ----------------------------------------------------------------------------------------------
        // modify RX PHASE TABLE to handle OFF CENTER FOV 2D in both cases or PHASE CYCLING
        // ----------------------------------------------------------------------------------------------
        //  calculate 2D rx phase shift
        fovPhase = getDouble(FIELD_OF_VIEW_PHASE);

        double deltaPhase_2D = -2 * Math.PI * off_center_distance_2D / (fovPhase);
        //  calculate 2D rx phase table

        double pas2D = Math.toDegrees(deltaPhase_2D);
        double indexMaxPhase2D = (acquisitionMatrixDimension2D - 1) / 2.0;

        setTableOrder(Rx_phase, Order.Two);
        generateTable(Rx_phase, acquisitionMatrixDimension2D, i -> {
            double txPhase = rfSpoilingIncrement * i * (i + 1) / 2.0;
            double offCenterPhase = pas2D * (i - indexMaxPhase2D);

            return MathUtility.positiveAngle(offCenterPhase - txPhase);
        });

        //fill the OFF_CENTER_FIELD_OF_VIEW_EFF User Parameter
        ArrayList<Number> off_center_distanceList = new ArrayList<>();
        off_center_distanceList.add(Math.round(off_center_distance_1D * Math.pow(10, 5)) / Math.pow(10, 5));
        off_center_distanceList.add(0);
        off_center_distanceList.add(0);

        getParam(OFF_CENTER_FIELD_OF_VIEW_EFF).setValue(off_center_distanceList);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //
    //                                                          END OF SEQUENCE GENERATOR
    //
    // *********************************************************************************************************************************************
    // *** END OF SEQUENCE GENERATOR *********  END OF SEQUENCE GENERATOR *********  END OF SEQUENCE GENERATOR ********* END OF SEQUENCE GENERATOR
    // *********************************************************************************************************************************************

    private double ceilToSubDecimal(double numberToBeRounded, double Order) {
        return Math.ceil(numberToBeRounded * Math.pow(10, Order)) / Math.pow(10, Order);
    }

    private double getTx_bandwidth_factor(U tx_shape, U tx_bandwith_factor_param3d) {
        double tx_bandwidth_factor;

        List<Double> tx_bandwith_factor_3D_table = getListDouble(tx_bandwith_factor_param3d);

            if ("GAUSSIAN".equalsIgnoreCase(getText(tx_shape))) {
                tx_bandwidth_factor = tx_bandwith_factor_3D_table.get(1);
            } else if ("SINC3".equalsIgnoreCase(getText(tx_shape))) {
                tx_bandwidth_factor = tx_bandwith_factor_3D_table.get(2);
            } else if ("SINC5".equalsIgnoreCase(getText(tx_shape))) {
                tx_bandwidth_factor = tx_bandwith_factor_3D_table.get(3);
            } else if ("RAMP".equalsIgnoreCase(getText(tx_shape))) {
                tx_bandwidth_factor = tx_bandwith_factor_3D_table.get(3);
            } else if ("SLR_8_5152".equalsIgnoreCase(getText(tx_shape))) {
                tx_bandwidth_factor = tx_bandwith_factor_3D_table.get(4);
            } else if ("SLR_4_2576".equalsIgnoreCase(getText(tx_shape))) {
                tx_bandwidth_factor = tx_bandwith_factor_3D_table.get(5);
            } else {
                tx_bandwidth_factor = tx_bandwith_factor_3D_table.get(0);
            }


        return tx_bandwidth_factor;
    }

    private double getOff_center_distance_1D_2D_3D(int dim) {
        List<Double> image_orientation = getListDouble(IMAGE_ORIENTATION_SUBJECT);
        double[] direction_index = new double[9];
        for (int i = 0; i < 6; i++) {
            direction_index[i] = image_orientation.get(i);
        }
        direction_index[6] = direction_index[1] * direction_index[5] - direction_index[2] * direction_index[4];
        direction_index[7] = direction_index[2] * direction_index[3] - direction_index[0] * direction_index[5];
        direction_index[8] = direction_index[0] * direction_index[4] - direction_index[1] * direction_index[3];

        double norm_vector_read = Math.sqrt(Math.pow(direction_index[0], 2) + Math.pow(direction_index[1], 2) + Math.pow(direction_index[2], 2));
        double norm_vector_phase = Math.sqrt(Math.pow(direction_index[3], 2) + Math.pow(direction_index[4], 2) + Math.pow(direction_index[5], 2));
        double norm_vector_slice = Math.sqrt(Math.pow(direction_index[6], 2) + Math.pow(direction_index[7], 2) + Math.pow(direction_index[8], 2));

        //Offset according to animal position
        double off_center_distance_Z = getDouble(OFF_CENTER_FIELD_OF_VIEW_Z);
        double off_center_distance_Y = getDouble(OFF_CENTER_FIELD_OF_VIEW_Y);
        double off_center_distance_X = getDouble(OFF_CENTER_FIELD_OF_VIEW_X);

        //Offset according to READ PHASE and SLICE
        double off_center_distance;
        switch (dim) {
            case 1:
                off_center_distance = off_center_distance_X * direction_index[0] / norm_vector_read + off_center_distance_Y * direction_index[1] / norm_vector_read + off_center_distance_Z * direction_index[2] / norm_vector_read;
                break;
            case 2:
                off_center_distance = off_center_distance_X * direction_index[3] / norm_vector_phase + off_center_distance_Y * direction_index[4] / norm_vector_phase + off_center_distance_Z * direction_index[5] / norm_vector_phase;
                break;
            case 3:
                off_center_distance = off_center_distance_X * direction_index[6] / norm_vector_slice + off_center_distance_Y * direction_index[7] / norm_vector_slice + off_center_distance_Z * direction_index[8] / norm_vector_slice;
                break;
            default:
                off_center_distance = 0;
                break;
        }
        return off_center_distance;
    }

    //<editor-fold defaultstate="collapsed" desc="Generated Code (RS2D)">
    protected void addUserParams() {
        addMissingUserParams(U.values());
    }

    public String getName() {
        return "GRADIENT_ECHO_3D";
    }

    public String getVersion() {
        return "base";
    }
    //</editor-fold>
}