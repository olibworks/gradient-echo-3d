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

    private double spectralWidth;
    private double tr;
    private double te;

    private double sliceThickness;
    private double fov;
    private double fovPhase;
    private double off_center_distance_1D;
    private double off_center_distance_2D;
    private double off_center_distance_3D;

    private double txLength90;

    private boolean isEnablePhase;
    private boolean isEnableSlice;
    private boolean isEnableRead;

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

        sliceThickness = getDouble(SLICE_THICKNESS);
        fov = getDouble(FIELD_OF_VIEW);
        fovPhase = getDouble(FIELD_OF_VIEW_PHASE);
        off_center_distance_1D = getDouble(OFF_CENTER_FIELD_OF_VIEW_1D);
        off_center_distance_2D = getDouble(OFF_CENTER_FIELD_OF_VIEW_2D);
        off_center_distance_3D = getDouble(OFF_CENTER_FIELD_OF_VIEW_3D);

        txLength90 = getDouble(TX_LENGTH);

        isEnablePhase = getBoolean(GRADIENT_ENABLE_PHASE);
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
        acquisitionMatrixDimension2D = (acquisitionMatrixDimension2D < 4) && isEnablePhase ? 4 : acquisitionMatrixDimension2D;

        nb_scan_2d = acquisitionMatrixDimension2D;

        // -----------------------------------------------
        // 3D managment 1/2: matrix & scan
        // ------------------------------------------------
        // MATRIX

        //Calculate the number of k-space lines acquired in the 3rd Dimension : acquisitionMatrixDimension3D
        acquisitionMatrixDimension3D = userMatrixDimension3D;
        nb_scan_3d = acquisitionMatrixDimension3D;
        getParam(NUMBER_OF_SHOOT_3D).setValue(nb_scan_3d);

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
        off_center_distance_3D = isEnableSlice ? off_center_distance_3D : 0;

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
        set(Grad_enable_phase_3D, isEnableSlice);
        set(Grad_enable_slice, isEnableSlice);

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
        int nb_shape_points = 128;

        // tx_phase_shape_90
        Shape tx_shape = getSequenceTable(Tx_shape);
        generateTable(Tx_shape, "Gaussian", nb_shape_points, 0.25, 100, false);


        // -----------------------------------------------
        // Calculation RF pulse parameters  2/3 : RF pulse & attenuation
        // -----------------------------------------------
        double flip_angle = getDouble(FLIP_ANGLE);
        double tx_amp_90;
        // TX parameters :  TXroute, Probe, Channels
        List<Integer> txRoute = getListInt(TX_ROUTE); // route TX through Cameleon

        double power_factor = Utility.powerFillingFactor(tx_shape);       // get RF pulse power factor from instrument to calculate RF pulse amplitude
        double instrument_length_90 = PowerComputation.getHardPulse90Width(nucleus.name());
        double instrument_power_90 = PowerComputation.getHardPulse90Power(nucleus.name()) / power_factor;
        // Hard pulse calibration for an angle other than 90 can be missing, in such case you can either specify a default value
        double instrument_length_180 = PowerComputation.getHardPulse180Width(nucleus.name());
        // or you can throw an exception that stop the compilation and notify the user.
        double instrument_power_180 = PowerComputation.getHardPulse180Power(nucleus.name()) / power_factor;
        double power_90 = instrument_power_90 * Math.pow(instrument_length_90 / txLength90, 2);
        double power_180 = instrument_power_180 * Math.pow(instrument_length_180 / txLength90, 2);

        if (power_180 > Hardware.getMaxRfPowerPulsed(nucleus.name())) {  // TX LENGTH 90 MIN
            double tx_length_90_min = Math.ceil(instrument_length_180 / Math.sqrt(Hardware.getMaxRfPowerPulsed(nucleus.name()) / instrument_power_180) * 10000) / 10000.0;
            notifyOutOfRangeParam(TX_LENGTH, tx_length_90_min, ((NumberParam) getParam(TX_LENGTH)).getMaxValue(), "Pulse length too short to reach RF power with this pulse shape");
            txLength90 = tx_length_90_min;
        }

        // Calculate Att to get a 180° RF pulse around 80% amp
        double tx_amp_180_desired = 80;     // set 180° RF puse arround 80% of Chameleon output
        int tx_att = PowerComputation.getTxAttenuation(txRoute.get(0), power_180, observeFrequency, tx_amp_180_desired);

        // Calculate amp with the new and real Att
        tx_amp_90 = PowerComputation.getTxAmplitude(txRoute.get(0), power_90, observeFrequency, tx_att);

        // set calculated parameters to display values & sequence
        this.getParam(TX_ATT).setValue(tx_att);            // display PULSE_ATT
        this.getParam(TX_AMP_90).setValue(tx_amp_90);     // display 90° amplitude
        set(Tx_att, tx_att);                   // set PULSE_ATT to sequence

        set(Time_tx, txLength90);
        set(Tx_amp, tx_amp_90 * flip_angle / 90); // set 90° RF pulse amplitude to sequence

        // -----------------------------------------------
        // Calculation RF pulse parameters  3/3: bandwidth
        // -----------------------------------------------
        double tx_bandwidth_factor_90 = 1.35; // for gaussian pulse
        double tx_bandwidth_90 = tx_bandwidth_factor_90 / txLength90;

        // ---------------------------------------------------------------------
        // calculate SLICE gradient amplitudes for RF pulses
        // ---------------------------------------------------------------------
        double slice_thickness_excitation = sliceThickness;
        //SLICE gradient amp for 90
        double grad_amp_slice_slice;
        grad_amp_slice_slice = (tx_bandwidth_90 / ((GradientMath.GAMMA / nucleus.getRatio()) * slice_thickness_excitation));
        grad_amp_slice_slice = grad_amp_slice_slice * 100.0 / gMax;

        if (grad_amp_slice_slice > 100) {      // SLICE THICKNESS Limit
            double slice_thickness_excitation_90_min = (tx_bandwidth_90 / ((GradientMath.GAMMA / nucleus.getRatio()) * gMax));
            notifyOutOfRangeParam(SLICE_THICKNESS, slice_thickness_excitation_90_min, ((NumberParam) getParam(SLICE_THICKNESS)).getMaxValue(), "Pulse length too short to reach this slice thickness");
            grad_amp_slice_slice = (tx_bandwidth_90 / ((GradientMath.GAMMA / nucleus.getRatio()) * slice_thickness_excitation)) * 100.0 / gMax;
        }
        set(Grad_amp_slice, grad_amp_slice_slice);

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

        // pre-calculate SLICE_refocusing
        double grad_ratio_slice_refoc = isEnableSlice ? getDouble(SLICE_REFOCUSING_GRADIENT_RATIO) : 0.0;   // get slice refocussing ratio
        double grad_area_slice_refoc, grad_area_slice_slice;

        grad_area_slice_slice = (txLength90 + grad_shape_rise_time) * grad_amp_slice_slice;   // area of read gradient %
        grad_area_slice_refoc = grad_area_slice_slice * grad_ratio_slice_refoc;

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

        // Check if enougth time for 2D_PHASE, 3D_PHASE SLICE_REF or READ_PREP
        double grad_area_sequence_max = 100 * (grad_phase_application_time + grad_shape_rise_time);
        double grad_area_max = Math.max(grad_area_slice_refoc, Math.max(grad_area_read_prep, grad_max_area_phase_2D));            // calculate the maximum gradient aera between SLICE REFOC & READ PREPHASING
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

        // calculate SLICE_refocusing
        double grad_amp_slice_refoc = grad_area_slice_refoc / (grad_phase_application_time + grad_shape_rise_time);
        set(Grad_amp_phase_3D, -grad_amp_slice_refoc);

        // calculate PHASE 2D ENCODING Gradient
        double grad_total_amp_phase_2D = grad_total_area_phase_2D / (grad_phase_application_time + grad_shape_rise_time);
        setTableOrder(Grad_amp_phase_2D, Order.Two);
        generateTable(Grad_amp_phase_2D, acquisitionMatrixDimension2D,
                i -> -(grad_index_max_phase_2D * grad_total_amp_phase_2D) + i * grad_total_amp_phase_2D / (acquisitionMatrixDimension2D - 1)
        );

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
        double grad_amp_spoiler_ph = grad_amp_spoiler_sl_ph_re.get(1);  //%
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
        double frame_acquisition_time = number_of_averages * nb_scan_3d * nb_scan_2d * tr;
        double total_acquisition_time = frame_acquisition_time * nb_scan_4d;
        getParam(SEQUENCE_TIME).setValue(total_acquisition_time);

        // ------------------------------------------------------------------
        // calculate TX FREQUENCY offsets tables for multi-slice acquisitions and
        // ------------------------------------------------------------------
        double spacing_between_slice = getDouble(SPACING_BETWEEN_SLICE);
        double grad_amp_slice_mTpm = (tx_bandwidth_90 / ((GradientMath.GAMMA / nucleus.getRatio()) * slice_thickness_excitation));
        double frequency_center_3D_90 = -grad_amp_slice_mTpm * off_center_distance_3D * (GradientMath.GAMMA / nucleus.getRatio());
        if (!isEnableSlice) {
            frequency_center_3D_90 = 0;
        }

        setTableOrder(Tx_freq_offset, Order.Three);
        double[] offset_table_90 = new double[acquisitionMatrixDimension3D];
        if (isEnableSlice) {
            //MULTI-PLANAR case : calculation of frequency offset table
            double multi_planar_fov = (acquisitionMatrixDimension3D - 1) * (spacing_between_slice + slice_thickness_excitation);
            double multi_planar_freq_offset_90 = multi_planar_fov * grad_amp_slice_mTpm * (GradientMath.GAMMA / nucleus.getRatio());
            double[] tx_frequency_offset_90_table = new double[acquisitionMatrixDimension3D];

            for (int i = 0; i < acquisitionMatrixDimension3D; i++) {
                double tx_frequency_offset = (multi_planar_freq_offset_90 / 2) - (acquisitionMatrixDimension3D == 1 ? 0 : i * multi_planar_freq_offset_90 / (acquisitionMatrixDimension3D - 1)) + frequency_center_3D_90;
                tx_frequency_offset_90_table[i] = tx_frequency_offset;
            }

            // set frequency offet table according to transform plugin
            for (int k = 0; k < acquisitionMatrixDimension3D; k++) {
                int[] indexScan = plugin.invTransf(0, 0, k, 0);
                int sliceNumber = indexScan[0] / (acquisitionMatrixDimension1D) + indexScan[2];
                offset_table_90[sliceNumber] = tx_frequency_offset_90_table[k];
            }

            set(Tx_freq_offset, offset_table_90);
        } else {
            set(Tx_freq_offset, 0);
        }

        // ------------------------------------------------------------------
        // calculate TX FREQUENCY offsets tables for multi-slice acquisitions and
        // ------------------------------------------------------------------
        Table freqoffset_tx_prep = getSequenceTable(Freq_offset_tx_prep);
        Table freqoffset_tx_comp = getSequenceTable(Freq_offset_tx_comp);
        freqoffset_tx_prep.setOrder(Order.Three);
        freqoffset_tx_comp.setOrder(Order.Three);
        freqoffset_tx_prep.clear();
        freqoffset_tx_comp.clear();
        if (acquisitionMatrixDimension3D > 1) {
            for (int k = 0; k < acquisitionMatrixDimension3D; k++) {
                freqoffset_tx_prep.add(-((offset_table_90[k] * txLength90 / 2.0) % 1) / grad_rise_time);
                freqoffset_tx_comp.add(-((offset_table_90[k] * txLength90 / 2.0) % 1) / grad_rise_time);
            }
        } else {
            freqoffset_tx_prep.add(-((frequency_center_3D_90 * txLength90 / 2.0) % 1) / grad_rise_time);
            freqoffset_tx_comp.add(-((frequency_center_3D_90 * txLength90 / 2.0) % 1) / grad_rise_time);
        }

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
        setTableOrder(Rx_phase, Order.Two);

        if (deltaPhase_2D != 0) {
            double pas_2D = Math.toDegrees(deltaPhase_2D);
            double index_max_phase_2D = (acquisitionMatrixDimension2D - 1) / 2.0; // symetric k'space around zero
            generateTable(Rx_phase, acquisitionMatrixDimension2D, i -> {
                double rx_phase_value = Math.round(MathUtility.positiveAngle(pas_2D * (i - index_max_phase_2D)) * 100.0) / 100.0;
                return (rx_phase_value == 360) ? 0 : rx_phase_value;
            });
        } else {
            set(Rx_phase, 0);
        }

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
        return "Sample MRI GE";
    }

    public String getVersion() {
        return "master";
    }
    //</editor-fold>
}