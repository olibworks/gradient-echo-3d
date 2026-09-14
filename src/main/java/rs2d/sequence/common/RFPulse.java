package rs2d.sequence.common;

import rs2d.commons.log.Log;
import rs2d.spinlab.data.transformPlugin.TransformPlugin;
import rs2d.spinlab.instrument.util.GradientMath;
import rs2d.spinlab.plugins.loaders.LoaderFactory;
import rs2d.spinlab.plugins.loaders.PluginLoaderInterface;
import rs2d.spinlab.sequence.Sequence;
import rs2d.spinlab.sequence.table.Shape;
import rs2d.spinlab.sequence.table.Table;
import rs2d.spinlab.sequence.table.Utility;
import rs2d.spinlab.sequence.table.generator.TableGeneratorInterface;
import rs2d.spinlab.sequenceGenerator.GeneratorSequenceParamEnum;
import rs2d.spinlab.tools.param.NumberParam;
import rs2d.spinlab.tools.param.Param;
import rs2d.spinlab.tools.table.Order;
import rs2d.spinlab.tools.utility.Nucleus;
import rs2d.spinlab.api.PowerComputation;
import rs2d.spinlab.api.Hardware;

import java.util.*;

import static java.util.stream.IntStream.*;
import static rs2d.sequence.common.MriMath.ceilToSubDecimal;
import static rs2d.sequence.common.MriMath.floorToSubDecimal;

/**
 * Class RFPulse: small Common Version 2.0S
 */

public class RFPulse {

    private static final double[] slrPowerFactors90 = {2.331, 2.331 * 0.72}; //slr power factors compared to not slr pulses
    private static final double[] slrPowerFactors180 = {3.879, 3.879 * 0.36};

    private Param channelAttParam = null;

    private String pulseName = null;
    private Table amplitudeTable = null;
    private Table phase = null;
    private Table FrequencyOffsetTable = null;

    private Table timeTable = null;
    private Shape shape = null;
    private Shape shapePhase = null;
    // Power and length saved in the instrument
    private double instrumentPower90 = Double.NaN; //instrument power depends on pulse shape (divided by the shape power factor)
    private double instrumentLength90 = Double.NaN;

    private double observeFrequency = Double.NaN;
    private Nucleus nucleus = Nucleus.H1;
    private int numberOfFreqOffset = -1;
    private Order FrequencyOffsetOrder = Order.FourLoopB;

    private boolean isSlr = false;
    private int slrIndex = -1;

    private double[] txFrequencyOffsetTable = null;

    private double pulseDuration = Double.NaN;
    private double powerPulse = Double.NaN;
    private double voltagePulse = Double.NaN;
    private double flipAngle = Double.NaN;
    private double txAmp = Double.NaN;

    //XG
    private double sincGenRampSlope = 0.2;
    private Table attOffsetTable = null;//new Table(0, NumberEnum.TxAtt);

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                  Constructor
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public RFPulse() {
    }

    public RFPulse(Param attPar, Table amplitudeTab, Table phaseTab,
                   Table timeTab, Shape shape, Shape shapePhase, Table offsetFreqTab, Nucleus nucleus) {
        amplitudeTable = amplitudeTab;
        channelAttParam = attPar;
        phase = phaseTab;

        timeTable = timeTab;
        this.shape = shape;
        this.shapePhase = shapePhase;

        pulseDuration = timeTable.get(0).doubleValue();
        FrequencyOffsetTable = offsetFreqTab;

        this.nucleus = nucleus;

    }


    public RFPulse(Table timeTab, Table offsetFreqTab, Nucleus nucleus) {
        timeTable = timeTab;
        pulseDuration = timeTab.get(0).doubleValue();
        FrequencyOffsetTable = offsetFreqTab;

        this.nucleus = nucleus;

    }

    public RFPulse(Table timeTab, Table offsetFreqTab, Table phaseTab, Nucleus nucleus) {
        timeTable = timeTab;
        phase = phaseTab;
        pulseDuration = timeTab.get(0).doubleValue();
        FrequencyOffsetTable = offsetFreqTab;

        this.nucleus = nucleus;
    }

    //XG
    public RFPulse(Param attPar, Table attOffsetPar, Table amplitudeTab, Table phaseTab,
                   Table timeTab, Shape shape, Shape shapePhase, Table offsetFreqTab, Nucleus nucleus) {
        //new RFPulse(attPar, amplitudeTab, phaseTab, timeTab, shape, shapePhase, offsetFreqTab);
        amplitudeTable = amplitudeTab;
        channelAttParam = attPar;
        phase = phaseTab;

        timeTable = timeTab;
        this.shape = shape;
        this.shapePhase = shapePhase;

        pulseDuration = timeTable.get(0).doubleValue();
        FrequencyOffsetTable = offsetFreqTab;
        if (attOffsetPar != null) {
            attOffsetTable = attOffsetPar;
        }

        this.nucleus = nucleus;

    }

    public static RFPulse createRFPulse(Sequence sequence, GeneratorSequenceParamEnum txAttParam, GeneratorSequenceParamEnum amplitudeTab, GeneratorSequenceParamEnum txPhaseTab,
                                        GeneratorSequenceParamEnum timeTab, GeneratorSequenceParamEnum shape, GeneratorSequenceParamEnum shapePhaseShape, GeneratorSequenceParamEnum offsetTab, Nucleus nucleus) {
        return new RFPulse(sequence.getPublicParam(txAttParam.name()), sequence.getTable(amplitudeTab.name()), sequence.getTable(txPhaseTab.name()),
                sequence.getPublicTable(timeTab.name()), (Shape) sequence.getTable(shape.name()), (Shape) sequence.getTable(shapePhaseShape.name()), sequence.getTable(offsetTab.name()), nucleus);
    }

    //XG
    public static RFPulse createRFPulse(Sequence sequence, GeneratorSequenceParamEnum txAttParam, GeneratorSequenceParamEnum txAttOffsetParam, GeneratorSequenceParamEnum amplitudeTab, GeneratorSequenceParamEnum txPhaseTab,
                                        GeneratorSequenceParamEnum timeTab, GeneratorSequenceParamEnum shape, GeneratorSequenceParamEnum shapePhaseShape, GeneratorSequenceParamEnum offsetTab, Nucleus nucleus) {
        return new RFPulse(sequence.getPublicParam(txAttParam.name()), sequence.getPublicTable(txAttOffsetParam.name()), sequence.getTable(amplitudeTab.name()), sequence.getTable(txPhaseTab.name()),
                sequence.getPublicTable(timeTab.name()), (Shape) sequence.getTable(shape.name()), (Shape) sequence.getTable(shapePhaseShape.name()), sequence.getTable(offsetTab.name()), nucleus);
    }

    public static RFPulse createRFPulse(Sequence sequence, GeneratorSequenceParamEnum timeTab, GeneratorSequenceParamEnum offsetTab, GeneratorSequenceParamEnum txPhaseTab, Nucleus nucleus) {
        return new RFPulse(sequence.getPublicTable(timeTab.name()), sequence.getTable(offsetTab.name()), sequence.getTable(txPhaseTab.name()), nucleus);
    }

    public static RFPulse createRFPulse(Sequence sequence, GeneratorSequenceParamEnum timeTab, GeneratorSequenceParamEnum offsetTab, Nucleus nucleus) {
        return new RFPulse(sequence.getPublicTable(timeTab.name()), sequence.getTable(offsetTab.name()), nucleus);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                  general  methods: get set
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public String getName() {
        return pulseName;
    }

    public double getPulseDuration() {
        return pulseDuration;
    }

    // get direct access to SP
    public Table getTimeTable() {
        return timeTable;
    }

    public double getAmp() {
        return txAmp;
    }

    public Table getAmpTable() {
        return amplitudeTable;
    }

    public double getAmpLimit(double observeFrequency, List<Integer> txRoute) {
        int channelAttOffset = (getAttOffset() != -1) ? getAttOffset() : 0; // If no attenuation offset has been set, consider it is 0
        return PowerComputation.getTxAmplitude(txRoute.get(0), Hardware.getMaxRfPowerPulsed(nucleus.name()), observeFrequency, getAtt() + channelAttOffset);
    }

    public double getFlipAngle() {
        if (Double.isNaN(flipAngle))
            prepFlipAngle();
        return flipAngle;
    }

    public double getPower() {
        if (Double.isNaN(powerPulse))
            prepPowerWithFlipAngle();
        return powerPulse;
    }

    public double getVoltage() {
        if (Double.isNaN(powerPulse))
            prepPowerWithFlipAngle();
        return voltagePulse;
    }

    public double getPowerGammaB1() {
        if (Double.isNaN(flipAngle))
            prepFlipAngle();
        double complexVoltageFactor = Math.sqrt(flipAngle < 135 ? getShapePowerFactor90() : getShapePowerFactor180());
        return flipAngle / (360 * complexVoltageFactor * pulseDuration);
    }

    public Order getFrequencyOffsetOrder() {
        return FrequencyOffsetOrder;
    }

    public int getNumberOfFreqOffset() {
        return numberOfFreqOffset;
    }

    public boolean isSlr() {
        return isSlr;
    }

    public double getSlrPowerFactors90() {
        return isSlr ? slrPowerFactors90[slrIndex] : 1.0;
    }

    public double getSlrPowerFactors180() {
        return isSlr ? slrPowerFactors180[slrIndex] : 1.0;
    }

    public double getShapePowerFactor90() {
        return Utility.complexPowerFillingFactor(shape, shapePhase) / getSlrPowerFactors90();
    }

    public double getShapePowerFactor180() {
        return Utility.complexPowerFillingFactor(shape, shapePhase) / getSlrPowerFactors180();
    }

    public int getAtt() {
        return ((NumberParam) channelAttParam).getValue().intValue();
    }

    public NumberParam getAttParam() {
        return (NumberParam) channelAttParam;
    }

    public int getAttOffset() {
        if (attOffsetTable != null)
            return attOffsetTable.get(0).intValue();
        else
            return -1;
    }

    public void setName(String RFPulseName) {
        pulseName = RFPulseName;
    }

    public void setPulseDuration(double pulse_duration) {
        pulseDuration = pulse_duration;
        setSequenceTableSingleValue(timeTable, pulseDuration);
    }

    protected void setAtt(int att) {
        channelAttParam.setValue(att);
    }

    protected void setAtt(NumberParam attParam) {
        channelAttParam = attParam;
    }

    protected void setAttOffset(int att_offset) {
        setSequenceTableSingleValue(attOffsetTable, att_offset);
    }

    public void setAttOffset(Table attTable) {
        setSequenceTableSingleValue(attOffsetTable, attTable.get(0).intValue());
    }

    public boolean createAttOffset(Sequence sequence, GeneratorSequenceParamEnum txAttOffsetParam) {
        if (sequence.getPublicTable(txAttOffsetParam.name()) != null) {
            attOffsetTable = sequence.getPublicTable(txAttOffsetParam.name());
            setAttOffset(0);
            return true;
        } else
            return false;
    }

    public void setAmp(double amp) {
        txAmp = amp;
        setSequenceTableSingleValue(amplitudeTable, txAmp);
    }

    public void setAmp(Order order, double... amps) {
        setSequenceTableValues(amplitudeTable, order, amps);
        if (amps.length > 0) {
            txAmp = amps[amps.length - 1];
        }
    }

    public void setAmp(NumberParam ampParam) {
        txAmp = ampParam.getValue().doubleValue();
        setSequenceTableSingleValue(amplitudeTable, txAmp);
    }


    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                  Static method to solve a list of pulses
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Automatically compute and set the channel attenuation and attenuation offsets in function of the RF-pulse powers
     *
     * @param txRoute          : Tx channel
     * @param observeFrequency :set pulse property
     * @param targetAmplitude  Amplitude to reach for the reference power
     * @param pulseList        List of RF-pulse power of the tx channel used to compute automatically channel attenuation and attenuation offset
     */
    public static void solvePulses(List<Integer> txRoute, double observeFrequency, double targetAmplitude, List<RFPulse> pulseList) throws Exception {

        // Remove disabled pulses from the list and retrieve the power for each pulse
        List<Double> RFPowerList = getPowerList(pulseList, true);

        //--------------------------------------------------
        // Construct the list of attenuations and set the channel attenuation
        // Compute attenuation to have percentAmp for each pule then set the channel attenuation to correspond to the maximum power
        List<Integer> attList = new ArrayList<>();
        int channelAtt;
        for (Double i_RF_power : RFPowerList) {
            attList.add(PowerComputation.getTxAttenuation(txRoute.get(0), i_RF_power, observeFrequency, targetAmplitude));
        }
        channelAtt = Math.min(Collections.min(attList), Hardware.getMaxAttenuation(txRoute.get(0), observeFrequency));

        //----------------------------------------------------
        // Compute the attenuation threshold for which an attenuation offset will be added
        int channelAttThreshold = getAttThreshold(attList);
        double channelPowerThreshold = RFPowerList.get(attList.indexOf(channelAttThreshold));
        int channelAttOffset = Math.min(channelAttThreshold, Hardware.getMaxAttenuation(txRoute.get(0), observeFrequency)) - channelAtt;

        //----------------------------------------------------
        // Set attenuation and attenuation offset in each pulse
        for (RFPulse eachPulse : pulseList) {
            eachPulse.setAtt(channelAtt);
            if (eachPulse.getPower() <= channelPowerThreshold) {
                eachPulse.setAttOffset(channelAttOffset);
            } else {
                eachPulse.setAttOffset(0);
            }
            eachPulse.prepTxAmp(txRoute);
        }
    }

    /**
     * Extract the power from each pulse of the input list of RFPulse
     *
     * @param pulseList        List of RFPulse object
     * @param bRemoveDuplicate Remove duplicate values
     * @return List of powers
     */
    public static List<Double> getPowerList(List<RFPulse> pulseList, boolean bRemoveDuplicate) {
        List<Double> RFPowerList = new ArrayList<>();
        for (RFPulse eachPulse : pulseList) {
            // At first compilation, the same RF Pulse object can be created multiple times,
            if ((!bRemoveDuplicate || !RFPowerList.contains(eachPulse.getPower())) && eachPulse.getFlipAngle() > 0.0) {
                RFPowerList.add(eachPulse.getPower());
            }
        }
        return RFPowerList;
    }

    /**
     * Get the attenuation threshold to set the attOffset
     *
     * @param attList List of attenuation for each pulse power
     * @return attenuation threshold
     */
    private static int getAttThreshold(List<Integer> attList) {
        // The attenuations are split into two groups: a group closer to the minimum value and a group closer to the maximum value (2nd group)
        // The threshold is defined to be the minimum value of the 2nd group
        // 1/  Remove all the points, that are closer of the minimum value than the maximum value
        int att_middle = Math.round((float) (Collections.max(attList) + Collections.min(attList)) / 2);
        List<Integer> att_with_offset_list = new ArrayList<>(attList);
        att_with_offset_list.removeIf(att -> (att - att_middle) < 0);
        // 2/ Look for the minimum value of the second group
        return Collections.min(att_with_offset_list);
    }


    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                  Solve methods for one pulse (set Amp/Att & check power)
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Automatic Calibration for a given Flip Angle with ATT set to get 180° -> 80%amp.
     * set amplitudeTable and channelAttParam
     *
     * @param txRoute          : Tx channel
     * @param observeFrequency :set pulse property
     * @param flipAngle        : flip angle of the pulse
     * @param targetAmplitude  : amplitude to reach for the maximum reference pulse power (either 180° Pulse or current power)
     * @return test_change_time : false if the pulse duration has changed
     */
    public boolean solveOnePulseWithFlipAngleAndReference180(List<Integer> txRoute, double observeFrequency, double flipAngle, double targetAmplitude) throws Exception {
        // Prepare power
        boolean b_time_unchanged = prepPowerWithFlipAngle(observeFrequency, flipAngle);
        // Prepare attenuation and check if it doesn't exceed hardware limit
        if (flipAngle < 180) { // it is always nice in the onePulse to directly have access to the 180° (att/amp auto not automatic) by changing the amp only.
            b_time_unchanged = b_time_unchanged && prepChannelAttWithPower180(txRoute, targetAmplitude); // the test to know if the length has to be increase has not been done for 180° pulse
        } else {
            prepChannelAttWithPower(txRoute, targetAmplitude);
        }
        // Prepare amplitude
        prepTxAmp(txRoute);

        return b_time_unchanged;
    }

    /**
     * Prepare the pulse with a given Voltage.
     * set amplitudeTable and attParam
     *
     * @param txRoute           : Tx channel
     * @param observe_frequency :set pulse property
     * @param targetAmplitude   : amplitude to achieve the wanted voltage
     * @param voltage           : voltage of the pulse
     * @return b_voltage_unchanged : false if the pulse voltage has changed
     */
    public boolean solveOnePulseWithVoltage(List<Integer> txRoute, double observe_frequency, double targetAmplitude, double voltage) throws Exception {
        //prepare power
        observeFrequency = observe_frequency;
        voltagePulse = voltage;
        powerPulse = voltPPToWatt(voltagePulse);
        // check if power exceed maximum limitation, if yes use the maximum power
        boolean b_voltage_unchanged = true;
        if (powerPulse > Hardware.getMaxRfPowerPulsed(nucleus.name())) {  // TX LENGTH 90 MIN
            voltagePulse = floorToSubDecimal(wattToVPP(Hardware.getMaxRfPowerPulsed(nucleus.name())), 1);
            powerPulse = voltPPToWatt(voltagePulse);
            b_voltage_unchanged = false;
        }
        prepFlipAngle();
        // Prepare attenuation and check if it doesn't exceed hardware limit
        prepChannelAttWithPower(txRoute, targetAmplitude);
        // Prepare amplitude
        prepTxAmp(txRoute);

        return b_voltage_unchanged;
    }

    /**
     * Prepare the pulse with amp/att
     *
     * @param txRoute          : Tx channel
     * @param observeFrequency : set pulse property
     * @param amp              : amplitude
     * @param att              : attenuation
     * @return b_input_unchanged : false if the pulse amplitude has changed
     */
    public boolean solveOnePulseWithAmpAtt(List<Integer> txRoute, double observeFrequency, double amp, int att) throws Exception {
        // set pulse parameters
        this.observeFrequency = observeFrequency;
        prepChannelAtt(att);
        setAmp(amp);
        // prepare the channel attenuation and power pulse (check if they don't exceed hardware limits)
        boolean b_input_unchanged = prepPowerWithAmpAtt(txRoute);
        // compute the corresponding flip angle
        prepFlipAngle();
        return b_input_unchanged;
    }

    /**
     * Prepare the pulse with amp/att
     *
     * @param amp              : amplitude
     * @param observeFrequency : set pulse property
     * @param txRoute          : Tx channel
     * @return b_voltage_unchanged : false if the pulse amplitude has changed
     */
    public boolean solveOnePulseWithAmp(List<Integer> txRoute, double observeFrequency, double amp) {
        setAmp(amp);
        this.observeFrequency = observeFrequency;
        boolean b_voltage_unchanged = prepPowerWithAmpAtt(txRoute);
        prepFlipAngle();
        return b_voltage_unchanged;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                  Prepare methods: Power, ChannelAtt, Amp
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    // ------------------------------ Power ------------------------------------------

    /**
     * Prepare the power needed to achieve the flipAngle and check if it exceeds the instrument power limit
     *
     * @param observeFrequency set pulse property
     * @param flipAngle        set pulse property
     * @return test_change_time = false if the pulseDuration was increase because of exceeding power max
     */
    public boolean prepPowerWithFlipAngle(double observeFrequency, double flipAngle) {
        this.flipAngle = flipAngle;
        this.observeFrequency = observeFrequency; // used to calculate the attenuation and amplitude that match with the power
        return prepPowerWithFlipAngle();
    }

    /**
     * calculate powerPulse from flipAngle and pulseDuration, and check if the power exceed the instrument power limit
     *
     * @return test_change_time = false if the pulseDuration was increase because of exceeding power max
     */
    private boolean prepPowerWithFlipAngle() {
        if (Double.isNaN(flipAngle) || Double.isNaN(pulseDuration)) {
            throw new IllegalArgumentException("Power Calculation cannot be done: Flip Angle and/or Pulse Duration are not set!"
                    + "Flip angle is " + flipAngle + ", pulse duration is " + pulseDuration + ".");
        }
        // The RF power is compute using the hard pulse calibration with the closest angle (known relation between power, duration and flip angle) and the shape power factor
        powerPulse = instrumentPower90 * Math.pow(flipAngle / 90, 2) * Math.pow(instrumentLength90 / pulseDuration, 2);
        // If the power exceed the instrument limit increase the pulse duration
        boolean b_time_unchanged = true;
        if (powerPulse > Hardware.getMaxRfPowerPulsed(nucleus.name())) {  // TX LENGTH 90 MIN
            double durationMin = ceilToSubDecimal(instrumentLength90 / Math.sqrt(Hardware.getMaxRfPowerPulsed(nucleus.name()) / (instrumentPower90 * Math.pow(flipAngle / 90, 2))), 6);
            setPulseDuration(durationMin);
            powerPulse = instrumentPower90 * Math.pow(flipAngle / 90, 2) * Math.pow(instrumentLength90 / pulseDuration, 2);
            b_time_unchanged = false;
        }
        voltagePulse = wattToVPP(powerPulse);

        return b_time_unchanged;
    }

    /**
     * Prepare power with amplitude and attenuation, and check if it exceeds the instrument power limit
     *
     * @param txRoute : Tx channel
     */
    private boolean prepPowerWithAmpAtt(List<Integer> txRoute) {
        int att_offset = (getAttOffset() != -1) ? getAttOffset() : 0; // If no attenuation offset has been set, consider it is 0
        powerPulse = PowerComputation.getPower(txRoute.get(0), observeFrequency, getAmp(), getAtt() + att_offset);
        voltagePulse = wattToVPP(powerPulse);
        // check power limit
        boolean b_voltage_unchanged = true;
        if (powerPulse > Hardware.getMaxRfPowerPulsed(nucleus.name())) {  // TX LENGTH 90 MIN
            powerPulse = floorToSubDecimal(Hardware.getMaxRfPowerPulsed(nucleus.name()), 2);
            voltagePulse = wattToVPP(powerPulse);
            // compute amp corresponding to the new power
            prepTxAmp(txRoute);
            b_voltage_unchanged = false;
        }
        return b_voltage_unchanged;
    }

    /**
     * check if the pulse power is larger than the maximum output from PRim, i.e. when Tx ATT  = 0 and TX AMP = 100%
     * Prepare the power needed to achieve the flipAngle and check if it exceeds the instrument power limit
     *
     * @param txRoute           : Tx channel
     * @param observe_frequency set pulse property
     * @return test_change_time = false if the pulseDuration was increase because of exceeding power max
     */
    public boolean checkPowerPrim(List<Integer> txRoute, double observe_frequency) {
        boolean test_change_time = true;
        double prim_power = PowerComputation.getPower(txRoute.get(0), observe_frequency, 100.0, 0);
        if (powerPulse > prim_power) {  // TX LENGTH 90 MIN
            pulseDuration = ceilToSubDecimal(pulseDuration / Math.sqrt(prim_power / powerPulse), 6);
            setSequenceTableSingleValue(timeTable, pulseDuration);
            powerPulse = prim_power;
            test_change_time = false;
        }

        return test_change_time;
    }

    // Prepare flip angle with power

    /**
     * Calculate and prepare the flip angle knowing the pulse power and the 90° calibration
     */
    private void prepFlipAngle() {
        if (Double.isNaN(powerPulse) || Double.isNaN(pulseDuration)) {
            throw new IllegalArgumentException("Flip angle Calculation cannot be done: pulse power and/or pulseDuration is not set!");
        }
        flipAngle = 90 * Math.sqrt(powerPulse / instrumentPower90) * pulseDuration / instrumentLength90;
    }

    // power-vpp conversion functions: Power = Voltage * Voltage / Impedance
    // Voltage sinusoid = Vpp / (2*sqrt(2)) and impedance RF pulse = 8
    public static double wattToVPP(double power) {
        return ceilToSubDecimal(Math.sqrt(power * (8 * 50)), 3);
    }

    public static double voltPPToWatt(double voltage) {
        return (voltage * voltage) / (8 * 50);
    }

    // ------------------------------ Attenuation ------------------------------------------

    /**
     * Check that the att (channel + offset) of the pulse does not exceed the att max allowed, and set the result into the sequence
     **/
    public void prepChannelAttAndOffset() throws Exception {
        prepChannelAttAndOffset(getAtt(), getAttOffset());
    }

    /**
     * Check that the att (channel + offset) of the pulse does not exceed the att max allowed, and set the result into the sequence
     *
     * @param att:       attenuation of the tx channel
     * @param attOffset: attenuation offset of the pulse
     * @throws Exception: Problem with the config of the instrument
     */
    public void prepChannelAttAndOffset(int att, int attOffset) throws Exception {
        prepChannelAtt(att);
        prepAttOffset(attOffset);
    }

    /**
     * Check that the channel att does not exceed the att max allowed (for linearity correction), and set the result into the sequence
     *
     * @param att: attenuation of the tx channel
     * @throws Exception: Problem with the config of the instrument
     */
    public void prepChannelAtt(int att) throws Exception {
        int attMax = Hardware.getMaxAttenuation(nucleus);
        att = Math.min(att, attMax);
        setAtt(att);
    }

    /**
     * Check that the att offset of the pulse does not exceed the att max allowed, and set the result into the sequence
     *
     * @param attOffset: attenuation offset of the pulse
     * @throws Exception: channel att exceed the att max (i.e. channel att has not been prepared yet)
     */
    public void prepAttOffset(int attOffset) throws Exception {
        int attOffsetMax = Hardware.getMaxAttenuation(nucleus) - getAtt();
        if (attOffsetMax < 0) {
            throw new Exception("Prepare the channel attenuation before the pulses' attenuation offset");
        }
        if (attOffset != -1) {
            attOffset = Math.min(attOffset, attOffsetMax);
            setAttOffset(attOffset);
        }
    }

    /**
     * calculate and set channelAtt in order to get the powerPulse at pulse amplitude = amp for a specific power
     *
     * @param txRoute         : Tx channel
     * @param targetAmplitude Amplitude to reach for the reference power
     */
    public void prepChannelAttWithPower(List<Integer> txRoute, double targetAmplitude) throws Exception {
        prepChannelAttWithPower(txRoute, targetAmplitude, powerPulse);
    }

    /**
     * calculate and set txAtt in order to get the powerPulse at pulse amplitude = amp for a specific power
     *
     * @param txRoute   : Tx channel
     * @param targetAmp Amplitude for refPower
     * @param refPower  Power used to choose the attenuation
     */
    public void prepChannelAttWithPower(List<Integer> txRoute, double targetAmp, double refPower) throws Exception {
        int channelAtt = PowerComputation.getTxAttenuation(txRoute.get(0), refPower, observeFrequency, targetAmp);
        prepChannelAtt(channelAtt);
    }

    /**
     * Automatic Calibration: ATT set to get 180°-> txAmp%.
     *
     * @param txRoute : Tx channel
     * @param txAmp   : of the 180° pulse
     * @return test_change_time : false if the pulse duration has changed
     */
    private boolean prepChannelAttWithPower180(List<Integer> txRoute, double txAmp) throws Exception {
        boolean b_time_unchanged = true;
        double powerPulse180 = 4 * instrumentPower90 * Math.pow(instrumentLength90 / pulseDuration, 2);

        if (powerPulse180 > Hardware.getMaxRfPowerPulsed(nucleus.name())) {  // TX LENGTH 90 MIN
            double durationMin = ceilToSubDecimal(instrumentLength90 / Math.sqrt(Hardware.getMaxRfPowerPulsed(nucleus.name()) / (4 * instrumentPower90)), 6);
            setPulseDuration(durationMin);
            powerPulse180 = 4 * instrumentPower90 * Math.pow(instrumentLength90 / pulseDuration, 2);
            b_time_unchanged = false;
        }
        // Calculate Att to get a 180° RF pulse around 80% amp and check if att doesn't exceed hardware limit
        prepChannelAttWithPower(txRoute, txAmp, powerPulse180);

        return b_time_unchanged;
    }

    // ------------------------------ Amplitude ------------------------------------------

    /**
     * prepare and set txAmp according to channelAtt , flipAngle
     *
     * @param txRoute : Tx channel
     */
    public void prepTxAmp(List<Integer> txRoute) {
        int att_offset = (getAttOffset() != -1) ? getAttOffset() : 0; // If no attenuation offset has been set, consider it is 0
        txAmp = PowerComputation.getTxAmplitude(txRoute.get(0), powerPulse, observeFrequency, getAtt() + att_offset);
        setSequenceTableSingleValue(amplitudeTable, txAmp);
    }

    /**
     * prepare and set txAmp according to SP attParam and flipAngle
     *
     * @param txRoute : Tx channel
     * @return txAmp
     */
    public double prepTxAmpMultiFA(List<Integer> txRoute, double[] FA_list, Order order) {
        double txAmp90 = calculateTxAmp90(txRoute);
        double txAmp180 = calculateTxAmp180(txRoute);
        setSequenceTableValues(amplitudeTable, order);
        range(0, FA_list.length).forEach(i -> {
            txAmp = (FA_list[i] < 135 ? txAmp90 : txAmp180) * FA_list[i] / (FA_list[i] < 135 ? 90 : 180);
            amplitudeTable.add(txAmp);
        });
        return txAmp;
    }

    public double prepTxAmpMultiFA(List<Integer> txRoute, List<Double> FA_list, Order order) {
        return prepTxAmpMultiFA(txRoute, FA_list.stream().mapToDouble(d -> d).toArray(), order);
    }

    /**
     * calculate 90° Pulse Amplitude for channelAtt
     *
     * @param txRoute : Tx channel
     * @return txAmp
     */
    private double calculateTxAmp90(List<Integer> txRoute) {
        int att_offset = (getAttOffset() != -1) ? getAttOffset() : 0; // If no attenuation offset has been set, consider it is 0
        return PowerComputation.getTxAmplitude(txRoute.get(0), instrumentPower90 * Math.pow(instrumentLength90 / pulseDuration, 2), observeFrequency, getAtt() + att_offset);
    }

    /**
     * calculate 180° Pulse Amplitude for txAtt
     *
     * @param txRoute : Tx channel
     * @return txAmp
     */
    private double calculateTxAmp180(List<Integer> txRoute) {
        int att_offset = (getAttOffset() != -1) ? getAttOffset() : 0; // If no attenuation offset has been set, consider it is 0
        return PowerComputation.getTxAmplitude(txRoute.get(0), 4 * instrumentPower90 * Math.pow(instrumentLength90 / pulseDuration, 2), observeFrequency, getAtt() + att_offset);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                  Shape
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Prepare the RF pulse shape
     *
     * @param pulseName     :GAUSSIAN , SINC3 , SINC5 , HARD
     * @param numberOfPoint The number of point of the generated shape
     * @param type          or "90 degree" "Refocusing (spin-echo)"
     */
    public void setShape(String pulseName, int numberOfPoint, String type) throws Exception {
        shapePhase.clear();
        if ("GAUSSIAN".equalsIgnoreCase(pulseName)) {
            setShapeTableValuesFromGaussGen(shape, numberOfPoint, 0.250, 100, false);
        } else if ("SINC3".equalsIgnoreCase(pulseName)) {
            setShapeTableValuesFromSincGen(shape, numberOfPoint, 2, 100, true, "Hamming");
            for (int i = 0; i < (numberOfPoint); i++) {
                shapePhase.add((i < ((int) (numberOfPoint / 4.0)))
                        || (i >= ((int) (numberOfPoint * 3.0 / 4.0))) ? 180 : 0);
            }
        } else if ("SINC5".equalsIgnoreCase(pulseName)) {
            setShapeTableValuesFromSincGen(shape, numberOfPoint, 3, 100, true, "Hamming");
            for (int i = 0; i < (numberOfPoint); i++) {
                shapePhase.add(((i > ((int) (numberOfPoint * 1.0 / 6.0)) && (i <= ((int) (numberOfPoint * 2.0 / 6.0))))
                        || (i > ((int) (numberOfPoint * 4.0 / 6.0)) && (i <= ((int) (numberOfPoint * 5.0 / 6.0)))))
                        ? 180 : 0);
            }
        } else if ("HARD".equalsIgnoreCase(pulseName)) {
            shape.clear();    // set to HARD pulse
            shapePhase.clear();    // set to HARD pulse
            shape.setFirst(100);
            shapePhase.setFirst(0);

        } else if ("RAMP".equalsIgnoreCase(pulseName)) {
            setTableValuesFromSincGenRamp(shape, numberOfPoint, 3, 100, true, "Hamming", sincGenRampSlope);
            setTableValuesFromSincGenRampPhase(shapePhase, numberOfPoint, 3, 100, true, "Hamming", sincGenRampSlope);
        } else if ("SLR_8_5152".equalsIgnoreCase(pulseName)) {
            shape.clear();
            shapePhase.clear();
            String bw = "8.5152";
            setTableValuesFromSLRGen(shape, numberOfPoint, 0, type, true, bw);
            setTableValuesFromSLRPhaseGen(shapePhase, numberOfPoint, 0, type, false, bw);
            isSlr = true;
            slrIndex = 0;
        } else if ("SLR_4_2576".equalsIgnoreCase(pulseName)) {
            shape.clear();
            shapePhase.clear();
            String bw = "4.2576";
            //type="Refocusing (spin-echo)";
            setTableValuesFromSLRGen(shape, numberOfPoint, 0, type, true, bw);
            setTableValuesFromSLRPhaseGen(shapePhase, numberOfPoint, 0, type, false, bw);
            isSlr = true;
            slrIndex = 1;
        } else if ("TONE".equalsIgnoreCase(pulseName)) {
            shape.clear();
            shapePhase.clear();
            setTableValuesFromToneGen(shape, numberOfPoint, 3, 100, false, "Hamming", 2, 0.1);
            setTableValuesFromTonePhaseGen(shapePhase, numberOfPoint, 3, 100, false, "Hamming", 2, 0.1);
        }

        instrumentPower90 = PowerComputation.getReferenceRect90Power(nucleus.name()) / getShapePowerFactor90();
        instrumentLength90 = PowerComputation.getReferenceRect90Width(nucleus.name());
    }

    /**
     * Generate a table of amplitude elements with an SLR pulse generator
     *
     * @param table    The table to be set
     * @param nbpoint  The number of point of the generated SLR pulse
     * @param amp      The amplitude of the generated SLR pulse (in %)
     * @param abs      True if you want the absolute values and false otherwise
     * @param bwstring Bandwidth
     */
    private void setTableValuesFromSLRGen(Table table, int nbpoint, double amp, String type, boolean abs, String bwstring) throws Exception {
        TableGeneratorInterface gen = loadTableGenerator("SLR");
        if (gen == null) {
            System.out.println(" no SLR Generator installed");
            Log.error(getClass(), " no SLR Generator installed");
        }
        gen.getParams().get(0).setValue(nbpoint);
        gen.getParams().get(1).setValue(bwstring);
        gen.getParams().get(2).setValue(amp);
        gen.getParams().get(3).setValue(type);//type
        gen.getParams().get(4).setValue(abs);//abs

        table.setGenerator(gen);
        if (gen == null) {
            table.clear();
            table.setFirst(100);
        } else {
            gen.generate();
        }
    }

    /**
     * Generate a table of phase elements with an SLR pulse generator
     *
     * @param table    The table to be set
     * @param nbpoint  The number of point of the generated SLRphase pulse
     * @param amp      The amplitude of the generated SLRphase pulse (in %)
     * @param abs      True if you want the absolute values and false otherwise
     * @param bwstring Bandwidth
     */
    private void setTableValuesFromSLRPhaseGen(Table table, int nbpoint, double amp, String type, boolean abs, String bwstring) throws Exception {
        TableGeneratorInterface gen = loadTableGenerator("SLRPhase");
        if (gen == null) {
            System.out.println(" no SLR Generator installed");
            Log.error(getClass(), " no SLR Generator installed");
        }
        gen.getParams().get(0).setValue(nbpoint);
        gen.getParams().get(1).setValue(bwstring);
        gen.getParams().get(2).setValue(amp);
        gen.getParams().get(3).setValue(type);//type
        gen.getParams().get(4).setValue(abs);//abs

        table.setGenerator(gen);
        if (gen == null) {
            table.clear();
            table.setFirst(100);
        } else {
            gen.generate();
        }
    }

    /**
     * Generate a table of amplitude elements with a Sinus Cardinal ramp generator
     *
     * @param table   The table to be set
     * @param nbpoint The number of point of the generated sinus cardinal ramp
     * @param nblobe  The number of lob of the generated sinus cardinal ramp
     * @param amp     The amplitude of the generated sinus cardinal ramp (in %)
     * @param abs     true if you want the absolute values and false otherwise
     * @param slope   Ramp slope
     */
    private void setTableValuesFromSincGenRamp(Table table, int nbpoint, int nblobe, double amp, Boolean abs, String window, double slope) throws Exception {
        TableGeneratorInterface gen = loadTableGenerator("SincRamp");
        gen.getParams().get(0).setValue(nbpoint);
        gen.getParams().get(1).setValue(nblobe);
        gen.getParams().get(2).setValue(amp);
        gen.getParams().get(3).setValue(abs);//abs
        gen.getParams().get(4).setValue(window);//abs
        gen.getParams().get(5).setValue(slope);

        table.setGenerator(gen);
        if (gen == null) {
            table.clear();
            table.setFirst(100);
        } else {
            gen.generate();
        }
    }

    /**
     * Generate a table of phase elements with a Sinus Cardinal ramp generator
     *
     * @param table   The table to be set
     * @param nbpoint The number of point of the generated sinus cardinal ramp
     * @param nblobe  The number of lob of the generated sinus cardinal ramp
     * @param amp     The amplitude of the generated sinus cardinal ramp (in %)
     * @param abs     true if you want the absolute values and false otherwise
     * @param slope   Ramp slope
     */
    private void setTableValuesFromSincGenRampPhase(Table table, int nbpoint, int nblobe, double amp, Boolean abs, String window, double slope) throws Exception {
        TableGeneratorInterface gen = loadTableGenerator("SincRampPhase");
        gen.getParams().get(0).setValue(nbpoint);
        gen.getParams().get(1).setValue(nblobe);
        gen.getParams().get(2).setValue(amp);
        gen.getParams().get(3).setValue(abs);//abs
        gen.getParams().get(4).setValue(window);//abs
        gen.getParams().get(5).setValue(slope);

        table.setGenerator(gen);
        if (gen == null) {
            table.clear();
            table.setFirst(100);
        } else {
            gen.generate();
        }
    }

    private void setTableValuesFromToneGen(Table table, int nbpoint, int nblobe, double amp, Boolean abs, String window, double slope, double slcborder) throws Exception {
        TableGeneratorInterface gen = loadTableGenerator("Tone");
        gen.getParams().get(0).setValue(nbpoint);
        gen.getParams().get(1).setValue(nblobe);
        gen.getParams().get(2).setValue(amp);
        gen.getParams().get(3).setValue(abs);//abs
        gen.getParams().get(4).setValue(window);//abs
        gen.getParams().get(5).setValue(slope);
        gen.getParams().get(6).setValue(slcborder);

        table.setGenerator(gen);
        if (gen == null) {
            table.clear();
            table.setFirst(100);
        } else {
            gen.generate();
        }
    }


    private void setTableValuesFromTonePhaseGen(Table table, int nbpoint, int nblobe, double amp, Boolean abs, String window, double slope, double slcborder) throws Exception {
        TableGeneratorInterface gen = null;
        gen = loadTableGenerator("TonePhase");
        gen.getParams().get(0).setValue(nbpoint);
        gen.getParams().get(1).setValue(nblobe);
        gen.getParams().get(2).setValue(amp);
        gen.getParams().get(3).setValue(abs);//abs
        gen.getParams().get(4).setValue(window);//abs
        gen.getParams().get(5).setValue(slope);
        gen.getParams().get(6).setValue(slcborder);

        table.setGenerator(gen);
        if (gen == null) {
            table.clear();
            table.setFirst(100);
        } else {
            gen.generate();
        }
    }

    /**
     * Table generator
     *
     * @param generatorName Name of the shape to generate
     * @return Table generator
     */
    private TableGeneratorInterface loadTableGenerator(String generatorName) throws Exception {
        TableGeneratorInterface gen = null;
        PluginLoaderInterface<TableGeneratorInterface> loader = LoaderFactory.getTableGeneratorPluginLoader();
        if (loader.containsPlugin(generatorName)) {
            gen = loader.getPluginByName(generatorName);
        }
        return gen;
    }

    /**
     * Generate a table of elements with a Sinus Cardinal generator
     *
     * @param table   The table to be set
     * @param nbpoint The number of point of the generated sinus cardinal
     * @param nblobe  The number of lob of the generated sinus cardinal
     * @param amp     The amplitude of the generated sinus cardinal (in %)
     * @param abs     true if you want the absolute values and false otherwise
     */
    private void setShapeTableValuesFromSincGen(Table table, int nbpoint, int nblobe, double amp, Boolean abs, String window) throws Exception {
        String name = "Sinus Cardinal with Apodisation";
        TableGeneratorInterface gen;
        gen = LoaderFactory.getTableGeneratorPluginLoader().getPluginByName(name);
        if (gen == null) {
            throw new IllegalStateException("Table generator not found: " + name);
        }
        table.setGenerator(gen);
        gen.getParams().get(0).setValue(nbpoint);
        gen.getParams().get(1).setValue(nblobe);
        gen.getParams().get(2).setValue(amp);
        gen.getParams().get(3).setValue(abs);//abs
        gen.getParams().get(4).setValue(window);//abs
        table.setGenerator(gen);
        gen.generate();
    }

    /**
     * Generate a table of element with a Gaussian generator
     *
     * @param table   The table to be set
     * @param nbpoint The number of point of the generated gaussian
     * @param width   The width of the generated gaussian
     * @param amp     The amplitude of the generated gaussian (in %)
     * @param abs     true if you want the absolute values and false otherwise
     */
    private void setShapeTableValuesFromGaussGen(Table table, int nbpoint, double width, double amp, Boolean abs) throws Exception {
        String name = "Gaussian";
        TableGeneratorInterface gen;
        gen = LoaderFactory.getTableGeneratorPluginLoader().getPluginByName(name);
        if (gen == null) {
            throw new IllegalStateException("Table generator not found: " + name);
        }
        table.setGenerator(gen);
        gen.getParams().get(0).setValue(nbpoint);
        gen.getParams().get(1).setValue(width);
        gen.getParams().get(2).setValue(amp);
        gen.getParams().get(3).setValue(abs);//abs

        gen.generate();

    }

    public void setSincGenRampSlope(double val) {
        sincGenRampSlope = val;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                  Offset Frequency
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * prepare txFrequencyOffsetTable for slice selection
     *
     * @param gradSlice             : Slice selection Gradient
     * @param nbSlice               : number of slice
     * @param spacing_between_slice : space between Slice
     * @param offCenterDistance3D   : offcenter FOV
     */
    public void prepareOffsetFreqMultiSlice(Gradient gradSlice, int nbSlice, double spacing_between_slice, double offCenterDistance3D) {
        numberOfFreqOffset = nbSlice;
        double grad_amp_slice_mTpm = gradSlice.getAmplitude_mTpm();
        double frequencyCenter3D90 = calculateOffsetFreq(grad_amp_slice_mTpm, offCenterDistance3D);
        double slice_thickness = Double.isNaN(gradSlice.getSliceThickness()) ? 0 : gradSlice.getSliceThickness();
        double multi_planar_fov = (numberOfFreqOffset - 1) * (spacing_between_slice + slice_thickness);
        double multiPlanarFreqOffset = multi_planar_fov * grad_amp_slice_mTpm * (GradientMath.GAMMA / nucleus.getRatio());
        txFrequencyOffsetTable = new double[numberOfFreqOffset];
        for (int i = 0; i < numberOfFreqOffset; i++) {
            double tx_frequency_offset = (multiPlanarFreqOffset / 2) - (numberOfFreqOffset == 1 ? 0 : i * multiPlanarFreqOffset / (numberOfFreqOffset - 1)) + frequencyCenter3D90;
            txFrequencyOffsetTable[i] = tx_frequency_offset;
        }
    }

    /**
     * reorder txFrequencyOffsetTable according to the plugin for interleaved multi-slices acquisition
     *
     * @param acquisitionPointsPerSlice  : acquired data point in a single scan
     * @param slicesAcquiredInSingleScan : number of acquired slice in a single scan
     */
    public void reoderOffsetFreq(TransformPlugin plugin, int acquisitionPointsPerSlice, int slicesAcquiredInSingleScan) {
        double sliceNumber;
        double[] offset_table = new double[numberOfFreqOffset];
        for (int k = 0; k < numberOfFreqOffset; k++) {
            int[] indexScan = plugin.invTransf(0, 0, k, 0);
            // slicesAcquiredInSingleScan are acquired in one TR so length index[2] = number of TR need to acquired all the slices
            // For the nth TR : index[0] = nTR * slicesAcquiredInSingleScan
            sliceNumber = (int) (indexScan[0] / (double) acquisitionPointsPerSlice) + indexScan[2] * slicesAcquiredInSingleScan;
            offset_table[(int) sliceNumber] = txFrequencyOffsetTable[k];
        }
        txFrequencyOffsetTable = offset_table;
    }

    public void reoderOffsetFreq(int slicesAcquiredInSingleScan) {
        if (Math.floor(numberOfFreqOffset / slicesAcquiredInSingleScan) * slicesAcquiredInSingleScan != numberOfFreqOffset)
            Log.error(getClass(), "floor(numberOfFreqOffset / slicesAcquiredInSingleScan) * slicesAcquiredInSingleScan != numberOfFreqOffset");

        double[] offset_table = new double[numberOfFreqOffset];
        int[] sliceNumber_table = new int[numberOfFreqOffset];
        for (int i = 0; i < Math.floor(numberOfFreqOffset / slicesAcquiredInSingleScan); i++) {
            for (int j = 0; j < slicesAcquiredInSingleScan; j++) {
                sliceNumber_table[slicesAcquiredInSingleScan * i + j] = i + (int) Math.floor(numberOfFreqOffset / slicesAcquiredInSingleScan) * j;
            }
        }

        for (int k = 0; k < numberOfFreqOffset; k++) {
            offset_table[k] = txFrequencyOffsetTable[sliceNumber_table[k]];
        }
        txFrequencyOffsetTable = offset_table;
    }

    /**
     * Calculate the frequency offset that corresponds to the off-center distance wanted given the gradient amplitude
     *
     * @param grad_amp_mTpm     : Amplitude of the gradient [mT/m]
     * @param offCenterDistance : Distance to the iso-center of the gradient
     * @return frequency offset
     */

    public double calculateOffsetFreq(double grad_amp_mTpm, double offCenterDistance) {
        return (-grad_amp_mTpm * offCenterDistance * (GradientMath.GAMMA / nucleus.getRatio()));
    }

    /**
     * Choose the loop that will increment the frequency offset and prepare FrequencyOffsetTable with the existing txFrequencyOffsetTable values
     *
     * @param order : loop order
     */

    public void setFrequencyOffset(Order order) {
        FrequencyOffsetOrder = order;
        setFrequencyOffset();
    }

    /**
     * Set txFrequencyOffsetTable to a specific value, and then prepare the FrequencyOffsetTable
     *
     * @param value : value of the frequency offset
     */
    public void setFrequencyOffset(double value) {
        txFrequencyOffsetTable = new double[1];
        txFrequencyOffsetTable[0] = value;
        numberOfFreqOffset = 1;
        setFrequencyOffset();
    }

    /**
     * Add frequency offsets at the end of tx offset array
     *
     * @param values : array of additional frequency offsets
     */
    public void addFrequencyOffset(double... values) {
        if (numberOfFreqOffset != -1) {
            double[] tmpTable = txFrequencyOffsetTable.clone();
            txFrequencyOffsetTable = new double[numberOfFreqOffset + values.length];
            System.arraycopy(tmpTable, 0, txFrequencyOffsetTable, 0, tmpTable.length);
            numberOfFreqOffset = numberOfFreqOffset + values.length;
        } else {
            numberOfFreqOffset = values.length;
            txFrequencyOffsetTable = new double[numberOfFreqOffset];
        }
        System.arraycopy(values, 0, txFrequencyOffsetTable, txFrequencyOffsetTable.length - values.length, values.length);
    }

    /**
     * Copy the values of txFrequencyOffsetTable into FrequencyOffsetTable and set the incremental loop order to FrequencyOffsetOrder
     */
    public void setFrequencyOffset() {
        setSequenceTableValues(FrequencyOffsetTable, FrequencyOffsetOrder);
        if (numberOfFreqOffset != -1) {
            for (int k = 0; k < numberOfFreqOffset; k++) {
                FrequencyOffsetTable.add(txFrequencyOffsetTable[k]);
            }
        } else {
            FrequencyOffsetTable.add(0);
        }
    }

    /**
     * Set txFrequencyOffsetTable, and then prepare the FrequencyOffsetTable values
     *
     * @param value : array of frequency offsets
     */
    public void setFrequencyOffset(double... value) {
        numberOfFreqOffset = value.length;
        txFrequencyOffsetTable = new double[numberOfFreqOffset];
        System.arraycopy(value, 0, txFrequencyOffsetTable, 0, numberOfFreqOffset);
        setFrequencyOffset();
    }

    /**
     * Prepare the FrequencyOffsetTable values and loop order
     *
     * @param order : loop order
     * @param value : array of frequency offsets
     */
    public void setFrequencyOffset(Order order, double... value) {
        setFrequencyOffset(value);
        setFrequencyOffset(order);
    }

    /**
     * Return the element at a given index from the txFrequencyOffsetTable Array
     *
     * @param k : array index
     * @return : k-th value of the txFrequencyOffsetTable
     */
    public double getFrequencyOffset(int k) {
        return txFrequencyOffsetTable[k];
    }

    /**
     * Calculate and set the FrequencyOffset to compensate another pulse
     *
     * @param pulse :  to compensate
     * @param ratio : ratio of the pule duration to be compensated
     */
    public void setCompensationFrequencyOffset(RFPulse pulse, double ratio) {
        FrequencyOffsetOrder = pulse.getFrequencyOffsetOrder();
        numberOfFreqOffset = pulse.getNumberOfFreqOffset();
        if (numberOfFreqOffset != -1) {
            txFrequencyOffsetTable = new double[numberOfFreqOffset];
            for (int k = 0; k < numberOfFreqOffset; k++) {
                txFrequencyOffsetTable[k] = -((pulse.getFrequencyOffset(k) * pulse.getPulseDuration() * ratio) % 1) / pulseDuration;
            }
        }
        setFrequencyOffset();
    }

    /**
     * calculate and set the FrequencyOffset to compensate another pulse
     * the pulse to be compensated should only have one offset frequency
     *
     * @param pulse :  RF object to compensate
     * @param order : Order of compensation frequency to be applied
     * @param times : time duration array of the pulse to be compensated
     */
    public void setCompensationFrequencyOffsetWithTime(RFPulse pulse, Order order, double... times) {
        if (pulse.getNumberOfFreqOffset() > 1) {
            setCompensationFrequencyOffsetWithTime(pulse, times[0]);
        }
        numberOfFreqOffset = times.length;

        txFrequencyOffsetTable = new double[numberOfFreqOffset];
        for (int k = 0; k < numberOfFreqOffset; k++) {
            txFrequencyOffsetTable[k] = -((pulse.getFrequencyOffset(0) * times[k]) % 1) / this.pulseDuration;
        }
        setFrequencyOffset(order);
    }

    /**
     * calculate and set the FrequencyOffset to compensate another pulse
     *
     * @param pulse :  to compensate
     * @param time  : time duration of the pulse to be compensated
     */
    public void setCompensationFrequencyOffsetWithTime(RFPulse pulse, double time) {
        FrequencyOffsetOrder = pulse.getFrequencyOffsetOrder();
        numberOfFreqOffset = pulse.getNumberOfFreqOffset();
        txFrequencyOffsetTable = new double[numberOfFreqOffset];
        for (int k = 0; k < numberOfFreqOffset; k++) {
            txFrequencyOffsetTable[k] = -((pulse.getFrequencyOffset(k) * time) % 1) / this.pulseDuration;
        }
        setFrequencyOffset();
    }

    /**
     * calculate and set the FrequencyOffset for and off center FOV 1D
     *
     * @param grad                :  readout Gradient
     * @param off_center_distance : off_center_distance to be compensated
     */
    public void setFrequencyOffsetReadout(Gradient grad, double off_center_distance) {
        numberOfFreqOffset = 1;
        double grad_amp_read_read_mTpm = grad.getAmplitude_mTpm();// amplitude in T/m
        txFrequencyOffsetTable = new double[numberOfFreqOffset];
        txFrequencyOffsetTable[0] = -grad_amp_read_read_mTpm * off_center_distance * (GradientMath.GAMMA / nucleus.getRatio());
        setSequenceTableValues(FrequencyOffsetTable, FrequencyOffsetOrder, txFrequencyOffsetTable[0]);
    }

    /**
     * Prepare the FrequencyOffsetTable for EPI readout (gradient inverted one echo over 2)
     *
     * @param grad                : gradient amplitude
     * @param off_center_distance : Distance to the iso-center of the gradient to be compensated
     * @param ETL                 : Echo train length (number of echoes that compose the echo train)
     * @param tableorder          : Loop order
     */
    public void setFrequencyOffsetReadoutEchoPlanar(Gradient grad, double off_center_distance, int ETL, Order tableorder) {
        numberOfFreqOffset = ETL;
        FrequencyOffsetOrder = tableorder;
        double grad_amp_read_read_mTpm = grad.getAmplitude_mTpm();// amplitude in T/m
        txFrequencyOffsetTable = new double[numberOfFreqOffset];
        for (int i = 0; i < numberOfFreqOffset; i++) {
            if (i % 2 == 0) {
                txFrequencyOffsetTable[i] = -grad_amp_read_read_mTpm * off_center_distance * (GradientMath.GAMMA / nucleus.getRatio());
            } else {
                txFrequencyOffsetTable[i] = grad_amp_read_read_mTpm * off_center_distance * (GradientMath.GAMMA / nucleus.getRatio());
            }
        }
        setSequenceTableValues(FrequencyOffsetTable, FrequencyOffsetOrder, txFrequencyOffsetTable);

    }

    /**
     * calculate and set the FrequencyOffset to induce a phase offset
     *
     * @param angleDegree : readout Gradient
     */
    public void setFrequencyOffsetForPhaseShift(double angleDegree) {
        numberOfFreqOffset = 1;
        txFrequencyOffsetTable = new double[numberOfFreqOffset];
        txFrequencyOffsetTable[0] = -Math.round((((angleDegree / 360.0) % 1.0) / this.pulseDuration));
        setSequenceTableValues(FrequencyOffsetTable, FrequencyOffsetOrder, txFrequencyOffsetTable[0]);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                  Phase
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Prepare the phase values and loop order
     *
     * @param order       : loop order
     * @param phaseValues : array of phase values
     */
    public void setPhase(Order order, double... phaseValues) {
        setSequenceTableValues(phase, order, phaseValues);
    }

    /**
     * Prepare the phase values, the fourth loop will be used
     *
     * @param phaseValues : array of phase values
     */
    public void setPhase(double phaseValues) {
        setSequenceTableSingleValue(phase, phaseValues);
    }

    // -------------------------------------------------------------------------------
    // ----------------- General Methods----------------------------------------------

    /**
     * Configure the sequence parameter table with specified values, the loop order is set to FourLoop
     *
     * @param table  : Table name
     * @param values : Table values
     */
    private void setSequenceTableSingleValue(Table table, double... values) {
        // uses Order.One because there are no tables in this dimension: compilation issue
        setSequenceTableValues(table, Order.FourLoop, values);
    }

    /**
     * Configure the sequence parameter table with specified values and loop order
     *
     * @param table  : Table name
     * @param order  : Loop order
     * @param values : Table values
     */
    private void setSequenceTableValues(Table table, Order order, double... values) {
        table.clear();
        table.setOrder(order);
        table.setLocked(true);
        for (double value : values) {
            table.add(value);
        }
    }

}