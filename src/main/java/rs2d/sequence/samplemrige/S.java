//-- generated code, will be overwritten at each recompilation

package rs2d.sequence.samplemrige;

import rs2d.spinlab.sequenceGenerator.GeneratorSequenceParamEnum;

public enum S implements GeneratorSequenceParamEnum {
    Grad_enable_phase_2D("Grad_enable_phase_2D"),
    Grad_enable_phase_3D("Grad_enable_phase_3D"),
    Grad_enable_read("Grad_enable_read"),
    Grad_enable_slice("Grad_enable_slice"),
    Grad_enable_spoiler_phase("Grad_enable_spoiler_phase"),
    Grad_enable_spoiler_read("Grad_enable_spoiler_read"),
    Grad_enable_spoiler_slice("Grad_enable_spoiler_slice"),
    Gradient_angle_phi("Gradient_angle_phi"),
    Gradient_angle_psi("Gradient_angle_psi"),
    Gradient_angle_theta("Gradient_angle_theta"),
    Gradient_axe_phase("Gradient_axe_phase"),
    Gradient_axe_read("Gradient_axe_read"),
    Intermediate_frequency("Intermediate_frequency"),
    Nb_1d("Nb_1d"),
    Nb_2d("Nb_2d"),
    Nb_3d("Nb_3d"),
    Nb_4d("Nb_4d"),
    Nb_point("Nb_point"),
    Pre_scan("Pre_scan"),
    Spectral_width("Spectral_width"),
    Tx_att("Tx_att"),
    Tx_frequency("Tx_frequency"),
    Tx_nucleus("Tx_nucleus"),
    Update_Dimension("Update Dimension"),
    Freq_offset_tx_comp("Freq_offset_tx_comp"),
    Freq_offset_tx_prep("Freq_offset_tx_prep"),
    FreqOffset_rx_comp("FreqOffset_rx_comp"),
    FreqOffset_rx_prep("FreqOffset_rx_prep"),
    Frequency_offset_init("Frequency_offset_init"),
    Grad_amp_phase_2D("Grad_amp_phase_2D"),
    Grad_amp_phase_3D("Grad_amp_phase_3D"),
    Grad_amp_read_prep("Grad_amp_read_prep"),
    Grad_amp_read_read("Grad_amp_read_read"),
    Grad_amp_slice("Grad_amp_slice"),
    Grad_amp_spoiler_phase("Grad_amp_spoiler_phase"),
    Grad_amp_spoiler_read("Grad_amp_spoiler_read"),
    Grad_amp_spoiler_slice("Grad_amp_spoiler_slice"),
    Grad_shape_down("Grad_shape_down"),
    Grad_shape_up("Grad_shape_up"),
    Rx_freq_offset("Rx_freq_offset"),
    Rx_gain("Rx_gain"),
    Rx_phase("Rx_phase"),
    Time_FIR_delay("Time_FIR_delay"),
    Time_grad_phase_top("Time_grad_phase_top"),
    Time_grad_ramp("Time_grad_ramp"),
    Time_min_instruction("Time_min_instruction"),
    Time_rx("Time_rx"),
    Time_te_delay1("Time_te_delay1"),
    Time_tr_delay("Time_tr_delay"),
    Time_tx("Time_tx"),
    Tx_amp("Tx_amp"),
    Tx_freq_offset("Tx_freq_offset"),
    Tx_phase("Tx_phase"),
    Tx_shape("Tx_shape"),
    Tx_shape_phase("Tx_shape_phase");

    //--

    private final String name;

    private S(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
