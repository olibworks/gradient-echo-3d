//-- generated code, will be overwritten at each recompilation

package rs2d.sequence.samplemrige;

import rs2d.spinlab.tools.param.*;
import rs2d.spinlab.tools.table.*;
import rs2d.spinlab.sequenceGenerator.GeneratorParamEnum;

import java.util.List;
import static java.util.Arrays.asList;

public enum U implements GeneratorParamEnum {
    ACCU_DIM("ACCU_DIM") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("ACCU_DIM");
            param.setDisplayedName("ACCU_DIM.name");
            param.setDescription("ACCU_DIM.description");
            param.setLocked(true);
            param.setGroup(EnumGroup.Scan);
            param.setCategory(Category.Acquisition);
            param.setUuid("f4024e91-940c-40f8-9c35-cab7128b2bc7");
            param.setNumberEnum(NumberEnum.Integer);
            param.setMinValue(0);
            param.setMaxValue(3);
            param.setValue(1);
            param.setDefaultValue(1);
            return param;
        }
    },

    ACQUISITION_MATRIX_DIMENSION_1D("ACQUISITION_MATRIX_DIMENSION_1D") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("ACQUISITION_MATRIX_DIMENSION_1D");
            param.setDisplayedName("Acquisition 1D");
            param.setDescription("The acquisition size of the first dimension");
            param.setLocked(true);
            param.setGroup(EnumGroup.Scan);
            param.setCategory(Category.Acquisition);
            param.setUuid("19727840-8fc6-4ea4-9e3f-6d4f94b34548");
            param.setNumberEnum(NumberEnum.Scan);
            param.setMinValue(0);
            param.setMaxValue(65536);
            param.setValue(81);
            param.setDefaultValue(128);
            return param;
        }
    },

    ACQUISITION_MATRIX_DIMENSION_2D("ACQUISITION_MATRIX_DIMENSION_2D") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("ACQUISITION_MATRIX_DIMENSION_2D");
            param.setDisplayedName("Acquisition 2D");
            param.setDescription("The acquisition size of the second dimension");
            param.setLocked(true);
            param.setGroup(EnumGroup.Scan);
            param.setCategory(Category.Acquisition);
            param.setUuid("5ef181be-208e-4922-86e1-b93be94d06b2");
            param.setNumberEnum(NumberEnum.Scan);
            param.setMinValue(0);
            param.setMaxValue(65536);
            param.setValue(80);
            param.setDefaultValue(128);
            return param;
        }
    },

    ACQUISITION_MATRIX_DIMENSION_3D("ACQUISITION_MATRIX_DIMENSION_3D") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("ACQUISITION_MATRIX_DIMENSION_3D");
            param.setDisplayedName("Acquisition 3D");
            param.setDescription("The acquisition size of the third dimension");
            param.setLocked(true);
            param.setGroup(EnumGroup.Scan);
            param.setCategory(Category.Acquisition);
            param.setUuid("777753bf-8797-4720-a7d1-df4d8bdf66bc");
            param.setNumberEnum(NumberEnum.Scan);
            param.setMinValue(0);
            param.setMaxValue(65536);
            param.setValue(4);
            param.setDefaultValue(1);
            return param;
        }
    },

    ACQUISITION_MATRIX_DIMENSION_4D("ACQUISITION_MATRIX_DIMENSION_4D") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("ACQUISITION_MATRIX_DIMENSION_4D");
            param.setDisplayedName("Acquisition 4D");
            param.setDescription("The acquisition size of the fourth dimension");
            param.setLocked(true);
            param.setGroup(EnumGroup.Scan);
            param.setCategory(Category.Acquisition);
            param.setUuid("2a1119a0-894b-4758-b5bf-81e5d914012f");
            param.setNumberEnum(NumberEnum.Scan);
            param.setMinValue(0);
            param.setMaxValue(65536);
            param.setValue(1);
            param.setDefaultValue(1);
            return param;
        }
    },

    ACQUISITION_MODE("ACQUISITION_MODE") {
        public Param build() {
            ListTextParam param = new ListTextParam();
            param.setName("ACQUISITION_MODE");
            param.setDisplayedName("ACQUISITION_MODE");
            param.setLocked(true);
            param.setGroup(EnumGroup.Miscellaneous);
            param.setCategory(Category.Acquisition);
            param.setUuid("094866ce-e282-44da-9185-d56e61b6f1df");
            param.setValue(asList("COMPLEX", "REAL", "REAL", "REAL"));
            param.setDefaultValue(asList("COMPLEX", "REAL", "REAL", "REAL"));
            return param;
        }
    },

    ACQUISITION_TIME_OFFSET("ACQUISITION_TIME_OFFSET") {
        public Param build() {
            ListNumberParam param = new ListNumberParam();
            param.setName("ACQUISITION_TIME_OFFSET");
            param.setDisplayedName("ACQUISITION_TIME_OFFSET");
            param.setDescription("Relative acquisition start times in Dynamic or MultiSeries scans");
            param.setGroup(EnumGroup.Delay);
            param.setCategory(Category.Acquisition);
            param.setUuid("ec2563c7-c157-4193-92a9-60dd1b54b049");
            param.setMinValue(0.0);
            param.setMaxValue(1.0E9);
            param.setNumberEnum(NumberEnum.Time);
            param.setValue(asListNumber(0.0, 0.004));
            param.setDefaultValue(asListNumber(0.0));
            return param;
        }
    },

    ACQUISITION_TIME_PER_SCAN("ACQUISITION_TIME_PER_SCAN") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("ACQUISITION_TIME_PER_SCAN");
            param.setDisplayedName("Acq Time");
            param.setDescription("The acquisition time per scan  ( SPECTRAL_WIDTH )");
            param.setGroup(EnumGroup.Reception);
            param.setCategory(Category.Acquisition);
            param.setUuid("41a8df21-0610-4198-a7ae-bc4ae191c031");
            param.setNumberEnum(NumberEnum.Time);
            param.setMinValue(0.0);
            param.setMaxValue(1.0E9);
            param.setValue(0.0040497408);
            param.setDefaultValue(1.0);
            return param;
        }
    },

    BASE_FREQ_1("BASE_FREQ_1") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("BASE_FREQ_1");
            param.setDisplayedName("Base Freq 1");
            param.setDescription("The base frequency of the first sequence channel");
            param.setLocked(true);
            param.setGroup(EnumGroup.Emission);
            param.setCategory(Category.Acquisition);
            param.setUuid("875dc91a-96e7-4857-8524-fd8a2b3e9e83");
            param.setNumberEnum(NumberEnum.Frequency);
            param.setMinValue(0.0);
            param.setMaxValue(3.0E9);
            param.setValue(2017567.2412297176);
            param.setDefaultValue(1.27552944E8);
            return param;
        }
    },

    BASE_FREQ_2("BASE_FREQ_2") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("BASE_FREQ_2");
            param.setDisplayedName("Base Freq 2");
            param.setDescription("The base frequency of the second sequence channel");
            param.setLocked(true);
            param.setGroup(EnumGroup.Emission);
            param.setCategory(Category.Acquisition);
            param.setUuid("a11f644d-af4e-4285-82a6-0426862d9320");
            param.setNumberEnum(NumberEnum.Frequency);
            param.setMinValue(0.0);
            param.setMaxValue(3.0E9);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    BASE_FREQ_3("BASE_FREQ_3") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("BASE_FREQ_3");
            param.setDisplayedName("Base Freq 3");
            param.setDescription("The base frequency of the third sequence channel");
            param.setLocked(true);
            param.setGroup(EnumGroup.Emission);
            param.setCategory(Category.Acquisition);
            param.setUuid("16d36deb-3f4a-40f7-8f27-6d788f0f379f");
            param.setNumberEnum(NumberEnum.Frequency);
            param.setMinValue(0.0);
            param.setMaxValue(3.0E9);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    BASE_FREQ_4("BASE_FREQ_4") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("BASE_FREQ_4");
            param.setDisplayedName("Base Freq 4");
            param.setDescription("The base frequency of the fourth sequence channel");
            param.setLocked(true);
            param.setGroup(EnumGroup.Emission);
            param.setCategory(Category.Acquisition);
            param.setUuid("a4af46c4-5a38-456f-8fcd-4d778d63ac28");
            param.setNumberEnum(NumberEnum.Frequency);
            param.setMinValue(0.0);
            param.setMaxValue(3.0E9);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    DATA_REPRESENTATION("DATA_REPRESENTATION") {
        public Param build() {
            ListTextParam param = new ListTextParam();
            param.setName("DATA_REPRESENTATION");
            param.setDisplayedName("DATA_REPRESENTATION");
            param.setLocked(true);
            param.setGroup(EnumGroup.Dimension);
            param.setCategory(Category.Miscellaneous);
            param.setUuid("3754e9a3-e7ed-4628-8e57-c99d0f308ac4");
            param.setValue(asList("COMPLEX", "REAL", "REAL", "REAL"));
            param.setDefaultValue(asList("COMPLEX", "REAL", "REAL", "REAL"));
            return param;
        }
    },

    DIGITAL_FILTER_REMOVED("DIGITAL_FILTER_REMOVED") {
        public Param build() {
            BooleanParam param = new BooleanParam();
            param.setName("DIGITAL_FILTER_REMOVED");
            param.setDisplayedName("Digital filter removed");
            param.setDescription("Data shift due to the digital filter are removed");
            param.setLocked(true);
            param.setGroup(EnumGroup.Reception);
            param.setCategory(Category.Acquisition);
            param.setUuid("7b9d71ca-6987-4460-80d7-6964c39b2963");
            param.setValue(false);
            param.setDefaultValue(true);
            return param;
        }
    },

    DIGITAL_FILTER_SHIFT("DIGITAL_FILTER_SHIFT") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("DIGITAL_FILTER_SHIFT");
            param.setDisplayedName("Digital filter shift");
            param.setDescription("Data shift due to the digital filter");
            param.setLocked(true);
            param.setGroup(EnumGroup.Reception);
            param.setCategory(Category.Acquisition);
            param.setUuid("0889f37f-b870-4743-a2cb-d04070c239ec");
            param.setNumberEnum(NumberEnum.Integer);
            param.setMinValue(-2147483648);
            param.setMaxValue(2147483647);
            param.setValue(0);
            param.setDefaultValue(34);
            return param;
        }
    },

    DUMMY_SCAN("DUMMY_SCAN") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("DUMMY_SCAN");
            param.setDisplayedName("DS");
            param.setDescription("Dummy Scan");
            param.setGroup(EnumGroup.Scan);
            param.setCategory(Category.Acquisition);
            param.setUuid("83bf94a6-2b05-490c-950f-4d4f4e4732a6");
            param.setNumberEnum(NumberEnum.Scan);
            param.setMinValue(0);
            param.setMaxValue(65536);
            param.setValue(0);
            param.setDefaultValue(128);
            return param;
        }
    },

    DWELL_TIME("DWELL_TIME") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("DWELL_TIME");
            param.setDisplayedName("DW");
            param.setDescription("Reception dwell time");
            param.setGroup(EnumGroup.Reception);
            param.setCategory(Category.Acquisition);
            param.setUuid("beff4a15-44d0-437f-9fe4-02357456014f");
            param.setNumberEnum(NumberEnum.Time);
            param.setMinValue(0.0);
            param.setMaxValue(1.0E9);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    ECHO_TIME("ECHO_TIME") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("ECHO_TIME");
            param.setDisplayedName("TE");
            param.setDescription("The echo time ( TE )");
            param.setGroup(EnumGroup.Delay);
            param.setCategory(Category.Acquisition);
            param.setUuid("5a45112e-15db-4edf-bb22-0c466ec07ab6");
            param.setNumberEnum(NumberEnum.Time);
            param.setMinValue(0.0);
            param.setMaxValue(1.0E9);
            param.setValue(0.0529);
            param.setDefaultValue(0.005);
            return param;
        }
    },

    ECHO_TRAIN_LENGTH("ECHO_TRAIN_LENGTH") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("ECHO_TRAIN_LENGTH");
            param.setDisplayedName("ETL");
            param.setDescription("The echo train length");
            param.setGroup(EnumGroup.Scan);
            param.setCategory(Category.Acquisition);
            param.setUuid("859f6b52-a283-4f5f-9984-57515a9c04ca");
            param.setNumberEnum(NumberEnum.Integer);
            param.setMinValue(-2147483648);
            param.setMaxValue(2147483647);
            param.setValue(1);
            param.setDefaultValue(1);
            return param;
        }
    },

    FIELD_OF_VIEW("FIELD_OF_VIEW") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("FIELD_OF_VIEW");
            param.setDisplayedName("FOV");
            param.setDescription("The field of view");
            param.setGroup(EnumGroup.Dimension);
            param.setCategory(Category.Acquisition);
            param.setUuid("1aa0b426-8a6a-4892-be05-7944dd76dcd0");
            param.setNumberEnum(NumberEnum.Length);
            param.setMinValue(0.001);
            param.setMaxValue(1.7976931348623157E308);
            param.setValue(0.25);
            param.setDefaultValue(0.6);
            return param;
        }
    },

    FIELD_OF_VIEW_3D("FIELD_OF_VIEW_3D") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("FIELD_OF_VIEW_3D");
            param.setDisplayedName("FOV Slice/3D");
            param.setDescription("Info: Field of view coverage along the direction orthogonal to the phase and frequency encoding directions");
            param.setGroup(EnumGroup.Dimension);
            param.setCategory(Category.Acquisition);
            param.setUuid("2c03f2bc-33e2-4c99-b2c9-dbbbba593bfb");
            param.setNumberEnum(NumberEnum.Length);
            param.setMinValue(1.0E-4);
            param.setMaxValue(0.5);
            param.setValue(0.25);
            param.setDefaultValue(0.1);
            return param;
        }
    },

    FIELD_OF_VIEW_PHASE("FIELD_OF_VIEW_PHASE") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("FIELD_OF_VIEW_PHASE");
            param.setDisplayedName("FIELD_OF_VIEW_PHASE");
            param.setDescription("Field of View in the phase encoding direction");
            param.setGroup(EnumGroup.Dimension);
            param.setCategory(Category.Acquisition);
            param.setUuid("9d9cd3f7-514e-41fa-a750-4545f24ab27d");
            param.setNumberEnum(NumberEnum.Length);
            param.setMinValue(0.01);
            param.setMaxValue(0.5);
            param.setValue(0.25);
            param.setDefaultValue(0.08);
            return param;
        }
    },

    FLIP_ANGLE("FLIP_ANGLE") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("FLIP_ANGLE");
            param.setDisplayedName("Flip angle");
            param.setDescription("The flip angle for the excitation");
            param.setGroup(EnumGroup.Emission);
            param.setCategory(Category.Acquisition);
            param.setUuid("95b2af30-048e-4244-af70-391618f194a7");
            param.setNumberEnum(NumberEnum.Angle);
            param.setMinValue(-360.0);
            param.setMaxValue(360.0);
            param.setValue(20.0);
            param.setDefaultValue(5.0);
            return param;
        }
    },

    GRADIENT_ENABLE_PHASE("GRADIENT_ENABLE_PHASE") {
        public Param build() {
            BooleanParam param = new BooleanParam();
            param.setName("GRADIENT_ENABLE_PHASE");
            param.setDisplayedName("GRADIENT_ENABLE_PHASE");
            param.setDescription("enable the phase encoding gradient");
            param.setGroup(EnumGroup.Gradient);
            param.setCategory(Category.Acquisition);
            param.setUuid("0060a746-8558-4a6b-b48f-c83ff712dda5");
            param.setValue(true);
            param.setDefaultValue(false);
            return param;
        }
    },

    GRADIENT_ENABLE_PHASE_3D("GRADIENT_ENABLE_PHASE_3D") {
        public Param build() {
            BooleanParam param = new BooleanParam();
            param.setName("GRADIENT_ENABLE_PHASE_3D");
            param.setDisplayedName("GRADIENT_ENABLE_PHASE_3D");
            param.setDescription("enables second phase encoding gradient");
            param.setGroup(EnumGroup.Scan);
            param.setCategory(Category.Acquisition);
            param.setUuid("593fa0de-c2ff-4da5-b4d0-bb3ac969caab");
            param.setValue(true);
            param.setDefaultValue(true);
            return param;
        }
    },

    GRADIENT_ENABLE_READ("GRADIENT_ENABLE_READ") {
        public Param build() {
            BooleanParam param = new BooleanParam();
            param.setName("GRADIENT_ENABLE_READ");
            param.setDisplayedName("GRADIENT_ENABLE_READ");
            param.setDescription("enable the read encoding gradient");
            param.setGroup(EnumGroup.Gradient);
            param.setCategory(Category.Acquisition);
            param.setUuid("e821c1d1-b328-4448-b19c-b6e721645eb3");
            param.setValue(true);
            param.setDefaultValue(false);
            return param;
        }
    },

    GRADIENT_ENABLE_SLICE("GRADIENT_ENABLE_SLICE") {
        public Param build() {
            BooleanParam param = new BooleanParam();
            param.setName("GRADIENT_ENABLE_SLICE");
            param.setDisplayedName("GRADIENT_ENABLE_SLICE");
            param.setDescription("enable the slice encoding gradient");
            param.setGroup(EnumGroup.Gradient);
            param.setCategory(Category.Acquisition);
            param.setUuid("23a9221d-c3b9-425d-a1c5-b7e87f5c81be");
            param.setValue(true);
            param.setDefaultValue(false);
            return param;
        }
    },

    GRADIENT_ENABLE_SPOILER("GRADIENT_ENABLE_SPOILER") {
        public Param build() {
            BooleanParam param = new BooleanParam();
            param.setName("GRADIENT_ENABLE_SPOILER");
            param.setDisplayedName("GRADIENT_ENABLE_SPOILER");
            param.setDescription("Enable gradient spoiler on the three direction");
            param.setGroup(EnumGroup.Gradient);
            param.setCategory(Category.Acquisition);
            param.setUuid("8ea066cb-1651-4397-8a90-26f5d34d3ce1");
            param.setValue(true);
            param.setDefaultValue(false);
            return param;
        }
    },

    GRADIENT_PHASE_APPLICATION_TIME("GRADIENT_PHASE_APPLICATION_TIME") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("GRADIENT_PHASE_APPLICATION_TIME");
            param.setDisplayedName("GRADIENT_PHASE_APPLICATION_TIME");
            param.setDescription("the application time of the phase encoding gradient");
            param.setGroup(EnumGroup.Delay);
            param.setCategory(Category.Acquisition);
            param.setUuid("a62eb416-14ba-4f3a-b26e-5fd24883f929");
            param.setNumberEnum(NumberEnum.Time);
            param.setMinValue(0.0);
            param.setMaxValue(1.0E9);
            param.setValue(0.00163);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    GRADIENT_RISE_TIME("GRADIENT_RISE_TIME") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("GRADIENT_RISE_TIME");
            param.setDisplayedName("GRADIENT_RISE_TIME");
            param.setDescription("The rise time of the gradient");
            param.setGroup(EnumGroup.Delay);
            param.setCategory(Category.Acquisition);
            param.setUuid("b3770481-5f9a-415a-a59b-4b655d7dbda9");
            param.setNumberEnum(NumberEnum.Time);
            param.setMinValue(0.0);
            param.setMaxValue(1.0E9);
            param.setValue(1.5E-4);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    GRAD_AMP_SPOILER_SL_PH_RE("GRAD_AMP_SPOILER_SL_PH_RE") {
        public Param build() {
            ListNumberParam param = new ListNumberParam();
            param.setName("GRAD_AMP_SPOILER_SL_PH_RE");
            param.setDisplayedName("GRAD_AMP_SPOILER_SL_PH_SL");
            param.setDescription("");
            param.setGroup(EnumGroup.Gradient);
            param.setCategory(Category.Acquisition);
            param.setUuid("c155ff5f-a567-4999-8d7a-83dd62fb2f3a");
            param.setMinValue(0.0);
            param.setMaxValue(100.0);
            param.setNumberEnum(NumberEnum.PERCENT);
            param.setValue(asListNumber(30.0, 5.0, 4.0));
            return param;
        }
    },

    IMAGE_ORIENTATION_SUBJECT("IMAGE_ORIENTATION_SUBJECT") {
        public Param build() {
            ListNumberParam param = new ListNumberParam();
            param.setName("IMAGE_ORIENTATION_SUBJECT");
            param.setDisplayedName("IMAGE_ORIENTATION_SUBJECT");
            param.setDescription("Direction cosines of the first row and the first column with respect to the subject");
            param.setGroup(EnumGroup.Dimension);
            param.setCategory(Category.Acquisition);
            param.setUuid("45ed40c6-9cd4-4925-a11a-773f12ce8d09");
            param.setMinValue(-1.7976931348623157E308);
            param.setMaxValue(1.7976931348623157E308);
            param.setNumberEnum(NumberEnum.Double);
            param.setValue(asListNumber(1.0, 0.0, 0.0, 0.0, -1.0, 0.0));
            param.setDefaultValue(asListNumber(1.0, 0.0, 0.0, 0.0, 1.0, 0.0));
            return param;
        }
    },

    INTERMEDIATE_FREQUENCY("INTERMEDIATE_FREQUENCY") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("INTERMEDIATE_FREQUENCY");
            param.setDisplayedName("INTERMEDIATE_FREQUENCY.name");
            param.setDescription("INTERMEDIATE_FREQUENCY.description");
            param.setLocked(true);
            param.setGroup(EnumGroup.Reception);
            param.setCategory(Category.Acquisition);
            param.setUuid("7b6db6ef-3fa3-4161-858d-38990489b00f");
            param.setNumberEnum(NumberEnum.Frequency);
            param.setMinValue(0.0);
            param.setMaxValue(3.0E9);
            param.setValue(1.251E7);
            param.setDefaultValue(1.251E7);
            return param;
        }
    },

    LAST_PUT("LAST_PUT") {
        public Param build() {
            ListNumberParam param = new ListNumberParam();
            param.setName("LAST_PUT");
            param.setDisplayedName("LAST_PUT");
            param.setDescription("LAST_PUT.description");
            param.setLocked(true);
            param.setGroup(EnumGroup.Reception);
            param.setCategory(Category.Acquisition);
            param.setUuid("93c008f1-d54c-4e8f-a46a-da3ca66e5500");
            param.setMinValue(-2147483648);
            param.setMaxValue(2147483647);
            param.setNumberEnum(NumberEnum.Integer);
            param.setValue(asListNumber(0, 0, 0));
            param.setDefaultValue(asListNumber(0, 0, 0));
            return param;
        }
    },

    MAGNETIC_FIELD_STRENGTH("MAGNETIC_FIELD_STRENGTH") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("MAGNETIC_FIELD_STRENGTH");
            param.setDisplayedName("B0");
            param.setDescription("The magnetic field strength");
            param.setLocked(true);
            param.setLockedToDefault(true);
            param.setGroup(EnumGroup.Miscellaneous);
            param.setCategory(Category.Acquisition);
            param.setUuid("4c38c158-986c-47fa-b9bb-d80fdb2200c2");
            param.setNumberEnum(NumberEnum.Integer);
            param.setMinValue(-2147483648);
            param.setMaxValue(2147483647);
            param.setValue(0);
            param.setDefaultValue(0);
            return param;
        }
    },

    MANUFACTURER("MANUFACTURER") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("MANUFACTURER");
            param.setDisplayedName("MANUFACTURER");
            param.setDescription("Manufacturer");
            param.setLocked(true);
            param.setLockedToDefault(true);
            param.setGroup(EnumGroup.Miscellaneous);
            param.setCategory(Category.Miscellaneous);
            param.setUuid("3a9f5751-de55-43e4-b398-c37d0bb03545");
            param.setValue("Manufacturer");
            param.setDefaultValue("Manufacturer");
            return param;
        }
    },

    MIN_RISE_TIME_FACTOR("MIN_RISE_TIME_FACTOR") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("MIN_RISE_TIME_FACTOR");
            param.setDisplayedName("MIN_RISE_TIME_FACTOR");
            param.setDescription("");
            param.setGroup(EnumGroup.Gradient);
            param.setCategory(Category.Acquisition);
            param.setUuid("f085de58-cdea-4b1f-9fc9-98e8892e54e2");
            param.setNumberEnum(NumberEnum.PERCENT);
            param.setMinValue(0.0);
            param.setMaxValue(100.0);
            param.setValue(100.0);
            param.setDefaultValue(100.0);
            return param;
        }
    },

    MODALITY("MODALITY") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("MODALITY");
            param.setDisplayedName("Modality");
            param.setDescription("The modality for the acquisition");
            param.setLocked(true);
            param.setGroup(EnumGroup.Miscellaneous);
            param.setCategory(Category.Acquisition);
            param.setUuid("69c3956e-4225-4e16-b7fb-595e4491f8d1");
            param.setValue("MRI");
            param.setDefaultValue("MRI");
            param.setSuggestedValues(asList("NMR", "MRI", "DEFAULT"));
            param.setRestrictedToSuggested(true);
            return param;
        }
    },

    MODEL_NAME("MODEL_NAME") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("MODEL_NAME");
            param.setDisplayedName("MODEL_NAME");
            param.setDescription("Model name");
            param.setLocked(true);
            param.setLockedToDefault(true);
            param.setGroup(EnumGroup.Miscellaneous);
            param.setCategory(Category.Miscellaneous);
            param.setUuid("61f753e2-3b32-480e-a576-d4ff3babcae1");
            param.setValue("Model name");
            param.setDefaultValue("Model name");
            return param;
        }
    },

    MULTI_PLANAR_EXCITATION("MULTI_PLANAR_EXCITATION") {
        public Param build() {
            BooleanParam param = new BooleanParam();
            param.setName("MULTI_PLANAR_EXCITATION");
            param.setDisplayedName("Multi planar");
            param.setDescription("Multi planar excitation");
            param.setLocked(true);
            param.setGroup(EnumGroup.Dimension);
            param.setCategory(Category.Acquisition);
            param.setUuid("5633daf4-e431-4cec-960b-b99f61079a71");
            param.setValue(true);
            param.setDefaultValue(true);
            return param;
        }
    },

    NUCLEUS_1("NUCLEUS_1") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("NUCLEUS_1");
            param.setDisplayedName("Nucleus 1");
            param.setDescription("The nucleus used for the first sequence channel");
            param.setLocked(true);
            param.setGroup(EnumGroup.Miscellaneous);
            param.setCategory(Category.Acquisition);
            param.setUuid("d9a8800d-b7f6-4a90-a9cd-c7445f3cce01");
            param.setValue("1H");
            param.setDefaultValue("1H");
            param.setSuggestedValues(asList("Y", "Other", "X", "1H", "2H", "3H", "3He", "6Li", "7Li", "9Be", "10B", "11B", "13C", "14N", "15N", "17O", "19F", "21Ne", "23Na", "25Mg", "27Al", "29Si", "31P", "33S", "35Cl", "37Cl", "39K", "40K", "41K", "43Ca", "45Sc", "47Ti", "49Ti", "50V", "51V", "53Cr", "55Mn", "57Fe", "59Co", "61Ni", "63Cu", "65Cu", "67Zn", "69Ga", "71Ga", "73Ge", "75As", "77Se", "79Br", "81Br", "83Kr", "85Rb", "87Rb", "87Sr", "89Y", "91Zr", "93Nb", "95Mo", "97Mo", "99Tc", "99Ru", "101Ru", "103Rh", "105Pd", "107Ag", "109Ag", "111Cd", "113Cd", "113In", "115Sn", "115In", "117Sn", "119Sn", "121Sb", "123Te", "123Sb", "125Te", "127I", "129Xe", "129mXe", "131Xe", "131mXe", "133Cs", "135Ba", "137Ba", "138La", "139La", "141Pr", "143Nd", "145Nd", "147Sm", "149Sm", "151Eu", "153Eu", "155Gd", "157Gd", "159Tb", "161Dy", "163Dy", "165Ho", "167Er", "169Tm", "171Yb", "173Yb", "175Lu", "176Lu", "177Hf", "179Hf", "181Ta", "183W", "185Re", "187Re", "187Os", "189Os", "191Ir", "193Ir", "195Pt", "197Au", "199Hg", "201Hg", "203Tl", "205Tl", "207Pb", "209Bi", "235U"));
            param.setRestrictedToSuggested(true);
            return param;
        }
    },

    NUCLEUS_2("NUCLEUS_2") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("NUCLEUS_2");
            param.setDisplayedName("Nucleus 2");
            param.setDescription("The nucleus used for the second sequence channel");
            param.setLocked(true);
            param.setGroup(EnumGroup.Miscellaneous);
            param.setCategory(Category.Acquisition);
            param.setUuid("66badbf4-b421-40a9-9b14-956a99f64941");
            param.setValue("1H");
            param.setDefaultValue("1H");
            param.setSuggestedValues(asList("Y", "Other", "X", "1H", "2H", "3H", "3He", "6Li", "7Li", "9Be", "10B", "11B", "13C", "14N", "15N", "17O", "19F", "21Ne", "23Na", "25Mg", "27Al", "29Si", "31P", "33S", "35Cl", "37Cl", "39K", "40K", "41K", "43Ca", "45Sc", "47Ti", "49Ti", "50V", "51V", "53Cr", "55Mn", "57Fe", "59Co", "61Ni", "63Cu", "65Cu", "67Zn", "69Ga", "71Ga", "73Ge", "75As", "77Se", "79Br", "81Br", "83Kr", "85Rb", "87Rb", "87Sr", "89Y", "91Zr", "93Nb", "95Mo", "97Mo", "99Tc", "99Ru", "101Ru", "103Rh", "105Pd", "107Ag", "109Ag", "111Cd", "113Cd", "113In", "115Sn", "115In", "117Sn", "119Sn", "121Sb", "123Te", "123Sb", "125Te", "127I", "129Xe", "129mXe", "131Xe", "131mXe", "133Cs", "135Ba", "137Ba", "138La", "139La", "141Pr", "143Nd", "145Nd", "147Sm", "149Sm", "151Eu", "153Eu", "155Gd", "157Gd", "159Tb", "161Dy", "163Dy", "165Ho", "167Er", "169Tm", "171Yb", "173Yb", "175Lu", "176Lu", "177Hf", "179Hf", "181Ta", "183W", "185Re", "187Re", "187Os", "189Os", "191Ir", "193Ir", "195Pt", "197Au", "199Hg", "201Hg", "203Tl", "205Tl", "207Pb", "209Bi", "235U"));
            param.setRestrictedToSuggested(true);
            return param;
        }
    },

    NUCLEUS_3("NUCLEUS_3") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("NUCLEUS_3");
            param.setDisplayedName("Nucleus 3");
            param.setDescription("The nucleus used for the third sequence channel");
            param.setLocked(true);
            param.setGroup(EnumGroup.Miscellaneous);
            param.setCategory(Category.Acquisition);
            param.setUuid("599849e8-43fa-45ad-8887-d0cfaf291273");
            param.setValue("1H");
            param.setDefaultValue("1H");
            param.setSuggestedValues(asList("Y", "Other", "X", "1H", "2H", "3H", "3He", "6Li", "7Li", "9Be", "10B", "11B", "13C", "14N", "15N", "17O", "19F", "21Ne", "23Na", "25Mg", "27Al", "29Si", "31P", "33S", "35Cl", "37Cl", "39K", "40K", "41K", "43Ca", "45Sc", "47Ti", "49Ti", "50V", "51V", "53Cr", "55Mn", "57Fe", "59Co", "61Ni", "63Cu", "65Cu", "67Zn", "69Ga", "71Ga", "73Ge", "75As", "77Se", "79Br", "81Br", "83Kr", "85Rb", "87Rb", "87Sr", "89Y", "91Zr", "93Nb", "95Mo", "97Mo", "99Tc", "99Ru", "101Ru", "103Rh", "105Pd", "107Ag", "109Ag", "111Cd", "113Cd", "113In", "115Sn", "115In", "117Sn", "119Sn", "121Sb", "123Te", "123Sb", "125Te", "127I", "129Xe", "129mXe", "131Xe", "131mXe", "133Cs", "135Ba", "137Ba", "138La", "139La", "141Pr", "143Nd", "145Nd", "147Sm", "149Sm", "151Eu", "153Eu", "155Gd", "157Gd", "159Tb", "161Dy", "163Dy", "165Ho", "167Er", "169Tm", "171Yb", "173Yb", "175Lu", "176Lu", "177Hf", "179Hf", "181Ta", "183W", "185Re", "187Re", "187Os", "189Os", "191Ir", "193Ir", "195Pt", "197Au", "199Hg", "201Hg", "203Tl", "205Tl", "207Pb", "209Bi", "235U"));
            param.setRestrictedToSuggested(true);
            return param;
        }
    },

    NUCLEUS_4("NUCLEUS_4") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("NUCLEUS_4");
            param.setDisplayedName("Nucleus 4");
            param.setDescription("The nucleus used for the fourth sequence channel");
            param.setLocked(true);
            param.setGroup(EnumGroup.Miscellaneous);
            param.setCategory(Category.Acquisition);
            param.setUuid("ca901a5f-05c8-4755-9d3d-54813eec6a7c");
            param.setValue("1H");
            param.setDefaultValue("1H");
            param.setSuggestedValues(asList("Y", "Other", "X", "1H", "2H", "3H", "3He", "6Li", "7Li", "9Be", "10B", "11B", "13C", "14N", "15N", "17O", "19F", "21Ne", "23Na", "25Mg", "27Al", "29Si", "31P", "33S", "35Cl", "37Cl", "39K", "40K", "41K", "43Ca", "45Sc", "47Ti", "49Ti", "50V", "51V", "53Cr", "55Mn", "57Fe", "59Co", "61Ni", "63Cu", "65Cu", "67Zn", "69Ga", "71Ga", "73Ge", "75As", "77Se", "79Br", "81Br", "83Kr", "85Rb", "87Rb", "87Sr", "89Y", "91Zr", "93Nb", "95Mo", "97Mo", "99Tc", "99Ru", "101Ru", "103Rh", "105Pd", "107Ag", "109Ag", "111Cd", "113Cd", "113In", "115Sn", "115In", "117Sn", "119Sn", "121Sb", "123Te", "123Sb", "125Te", "127I", "129Xe", "129mXe", "131Xe", "131mXe", "133Cs", "135Ba", "137Ba", "138La", "139La", "141Pr", "143Nd", "145Nd", "147Sm", "149Sm", "151Eu", "153Eu", "155Gd", "157Gd", "159Tb", "161Dy", "163Dy", "165Ho", "167Er", "169Tm", "171Yb", "173Yb", "175Lu", "176Lu", "177Hf", "179Hf", "181Ta", "183W", "185Re", "187Re", "187Os", "189Os", "191Ir", "193Ir", "195Pt", "197Au", "199Hg", "201Hg", "203Tl", "205Tl", "207Pb", "209Bi", "235U"));
            param.setRestrictedToSuggested(true);
            return param;
        }
    },

    NUMBER_OF_AVERAGES("NUMBER_OF_AVERAGES") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("NUMBER_OF_AVERAGES");
            param.setDisplayedName("NEX");
            param.setDescription("The number of average");
            param.setGroup(EnumGroup.Scan);
            param.setCategory(Category.Acquisition);
            param.setUuid("507305bc-4ec5-4d10-9140-861152fe24fc");
            param.setNumberEnum(NumberEnum.Scan);
            param.setMinValue(0);
            param.setMaxValue(65536);
            param.setValue(1);
            param.setDefaultValue(1);
            return param;
        }
    },

    NUMBER_OF_SHOOT_3D("NUMBER_OF_SHOOT_3D") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("NUMBER_OF_SHOOT_3D");
            param.setDisplayedName("NUMBER_OF_SHOOT_3D.name");
            param.setDescription("the number of scan 3D for transform 3D");
            param.setLocked(true);
            param.setGroup(EnumGroup.Scan);
            param.setCategory(Category.Acquisition);
            param.setUuid("a332f7e4-6c06-43e0-bcf2-e47496f79565");
            param.setNumberEnum(NumberEnum.Integer);
            param.setMinValue(1);
            param.setMaxValue(2147483647);
            param.setValue(4);
            param.setDefaultValue(4);
            return param;
        }
    },

    OBSERVED_FREQUENCY("OBSERVED_FREQUENCY") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("OBSERVED_FREQUENCY");
            param.setDisplayedName("Observed frequency");
            param.setDescription("The frequency of the acquisition");
            param.setLocked(true);
            param.setGroup(EnumGroup.Reception);
            param.setCategory(Category.Acquisition);
            param.setUuid("c80ec521-1be2-49fd-b745-5a211ba5d3cd");
            param.setNumberEnum(NumberEnum.Frequency);
            param.setMinValue(0.0);
            param.setMaxValue(3.0E9);
            param.setValue(2017567.2412297176);
            param.setDefaultValue(6.3E7);
            return param;
        }
    },

    OBSERVED_NUCLEUS("OBSERVED_NUCLEUS") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("OBSERVED_NUCLEUS");
            param.setDisplayedName("Observed Nucleus");
            param.setDescription("The observed nucleus");
            param.setLocked(true);
            param.setGroup(EnumGroup.Reception);
            param.setCategory(Category.Acquisition);
            param.setUuid("e78546a0-5045-41ea-abf8-9c4439796f21");
            param.setValue("1H");
            param.setDefaultValue("1H");
            param.setSuggestedValues(asList("Other", "Y", "X", "3H", "1H", "19F", "3He", "205Tl", "203Tl", "31P", "7Li", "119Sn", "117Sn", "87Rb", "115Sn", "11B", "125Te", "141Pr", "71Ga", "65Cu", "129Xe", "81Br", "63Cu", "23Na", "51V", "123Te", "27Al", "13C", "79Br", "151Eu", "55Mn", "93Nb", "45Sc", "159Tb", "69Ga", "121Sb", "59Co", "187Re", "185Re", "99Tc", "113Cd", "115In", "113In", "195Pt", "165Ho", "111Cd", "207Pb", "127I", "29Si", "77Se", "199Hg", "171Yb", "75As", "209Bi", "2H", "6Li", "139La", "9Be", "17O", "138La", "133Cs", "123Sb", "181Ta", "175Lu", "137Ba", "153Eu", "10B", "15N", "50V", "135Ba", "35Cl", "85Rb", "91Zr", "61Ni", "169Tm", "131Xe", "37Cl", "176Lu", "21Ne", "189Os", "33S", "14N", "43Ca", "97Mo", "201Hg", "95Mo", "67Zn", "25Mg", "40K", "53Cr", "49Ti", "47Ti", "143Nd", "101Ru", "89Y", "173Yb", "163Dy", "39K", "109Ag", "99Ru", "105Pd", "87Sr", "147Sm", "183W", "107Ag", "157Gd", "177Hf", "83Kr", "73Ge", "149Sm", "161Dy", "145Nd", "57Fe", "103Rh", "155Gd", "167Er", "41K", "179Hf", "187Os", "193Ir", "235U", "197Au", "191Ir"));
            param.setRestrictedToSuggested(true);
            return param;
        }
    },

    OFFSET_FREQ_1("OFFSET_FREQ_1") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("OFFSET_FREQ_1");
            param.setDisplayedName("Offset 1");
            param.setDescription("The offset frequency of the first sequence channel");
            param.setGroup(EnumGroup.Emission);
            param.setCategory(Category.Acquisition);
            param.setUuid("4ffb7991-74f2-4e49-b8d8-8223d6f9c254");
            param.setNumberEnum(NumberEnum.FrequencyOffset);
            param.setMinValue(-1.5E9);
            param.setMaxValue(1.5E9);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    OFFSET_FREQ_2("OFFSET_FREQ_2") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("OFFSET_FREQ_2");
            param.setDisplayedName("Offset 2");
            param.setDescription("The offset frequency of the second sequence channel");
            param.setLocked(true);
            param.setGroup(EnumGroup.Emission);
            param.setCategory(Category.Acquisition);
            param.setUuid("be254c93-5400-4145-af13-06dfe03c6ad3");
            param.setNumberEnum(NumberEnum.FrequencyOffset);
            param.setMinValue(-1.5E9);
            param.setMaxValue(1.5E9);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    OFFSET_FREQ_3("OFFSET_FREQ_3") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("OFFSET_FREQ_3");
            param.setDisplayedName("Offset 3");
            param.setDescription("The offset frequency of the third sequence channel");
            param.setLocked(true);
            param.setGroup(EnumGroup.Emission);
            param.setCategory(Category.Acquisition);
            param.setUuid("03d721b9-1088-46bb-96c5-7e71939fec85");
            param.setNumberEnum(NumberEnum.FrequencyOffset);
            param.setMinValue(-1.5E9);
            param.setMaxValue(1.5E9);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    OFFSET_FREQ_4("OFFSET_FREQ_4") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("OFFSET_FREQ_4");
            param.setDisplayedName("Offset 4");
            param.setDescription("The offset frequency of the fourth sequence channel");
            param.setLocked(true);
            param.setGroup(EnumGroup.Emission);
            param.setCategory(Category.Acquisition);
            param.setUuid("2a13fe80-332c-47c9-97f1-eacdca04fbcd");
            param.setNumberEnum(NumberEnum.FrequencyOffset);
            param.setMinValue(-1.5E9);
            param.setMaxValue(1.5E9);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    OFF_CENTER_FIELD_OF_VIEW_1D("OFF_CENTER_FIELD_OF_VIEW_1D") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("OFF_CENTER_FIELD_OF_VIEW_1D");
            param.setDisplayedName("Location 1D");
            param.setDescription("Offset in Readout Direction");
            param.setGroup(EnumGroup.Gradient);
            param.setCategory(Category.Acquisition);
            param.setUuid("a1cacd67-fe60-4a86-8055-5e6e31e31422");
            param.setNumberEnum(NumberEnum.Location);
            param.setMinValue(-1.7976931348623157E308);
            param.setMaxValue(1.7976931348623157E308);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    OFF_CENTER_FIELD_OF_VIEW_2D("OFF_CENTER_FIELD_OF_VIEW_2D") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("OFF_CENTER_FIELD_OF_VIEW_2D");
            param.setDisplayedName("Location 2D");
            param.setDescription("Offset in Phase Encoding Direction");
            param.setLocked(true);
            param.setGroup(EnumGroup.Gradient);
            param.setCategory(Category.Acquisition);
            param.setUuid("cf183b8b-8630-4b42-b05f-271a1bc68e9b");
            param.setNumberEnum(NumberEnum.Location);
            param.setMinValue(-1.7976931348623157E308);
            param.setMaxValue(1.7976931348623157E308);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    OFF_CENTER_FIELD_OF_VIEW_3D("OFF_CENTER_FIELD_OF_VIEW_3D") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("OFF_CENTER_FIELD_OF_VIEW_3D");
            param.setDisplayedName("Location Z");
            param.setDescription("Offset in the slice direction");
            param.setLocked(true);
            param.setGroup(EnumGroup.Gradient);
            param.setCategory(Category.Acquisition);
            param.setUuid("4a79c482-3b18-4cb4-8ae3-8665624b438d");
            param.setNumberEnum(NumberEnum.Location);
            param.setMinValue(-1.7976931348623157E308);
            param.setMaxValue(1.7976931348623157E308);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    OFF_CENTER_FIELD_OF_VIEW_EFF("OFF_CENTER_FIELD_OF_VIEW_EFF") {
        public Param build() {
            ListNumberParam param = new ListNumberParam();
            param.setName("OFF_CENTER_FIELD_OF_VIEW_EFF");
            param.setDisplayedName("OFF_CENTER_FIELD_OF_VIEW_EFF");
            param.setDescription("Offcenter effective in 1D 2D and 3D (read phase slice)");
            param.setGroup(EnumGroup.Dimension);
            param.setCategory(Category.Acquisition);
            param.setUuid("2ca06439-f4e3-463f-816d-a9f6630f00c1");
            param.setMinValue(-1.7976931348623157E308);
            param.setMaxValue(1.7976931348623157E308);
            param.setNumberEnum(NumberEnum.Location);
            param.setValue(asListNumber(0.0, 0.0, 0.0));
            param.setDefaultValue(asListNumber(0.0, 0.0, 0.0));
            return param;
        }
    },

    OFF_CENTER_FIELD_OF_VIEW_X("OFF_CENTER_FIELD_OF_VIEW_X") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("OFF_CENTER_FIELD_OF_VIEW_X");
            param.setDisplayedName("Location X");
            param.setDescription("Location in the R/L direction");
            param.setGroup(EnumGroup.Gradient);
            param.setCategory(Category.Acquisition);
            param.setUuid("0442494d-e698-4917-bbad-3be9a3de7a96");
            param.setNumberEnum(NumberEnum.Location);
            param.setMinValue(-1.7976931348623157E308);
            param.setMaxValue(1.7976931348623157E308);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    OFF_CENTER_FIELD_OF_VIEW_Y("OFF_CENTER_FIELD_OF_VIEW_Y") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("OFF_CENTER_FIELD_OF_VIEW_Y");
            param.setDisplayedName("Location Y");
            param.setDescription("Location in the A/P direction");
            param.setGroup(EnumGroup.Gradient);
            param.setCategory(Category.Acquisition);
            param.setUuid("9f99a6ef-98d4-4107-bcfa-c65a7199ff0a");
            param.setNumberEnum(NumberEnum.Location);
            param.setMinValue(-1.7976931348623157E308);
            param.setMaxValue(1.7976931348623157E308);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    OFF_CENTER_FIELD_OF_VIEW_Z("OFF_CENTER_FIELD_OF_VIEW_Z") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("OFF_CENTER_FIELD_OF_VIEW_Z");
            param.setDisplayedName("Location Z");
            param.setDescription("Location in the I/S direction");
            param.setGroup(EnumGroup.Gradient);
            param.setCategory(Category.Acquisition);
            param.setUuid("c9100f02-dd3d-48e9-be22-b558f693cb9d");
            param.setNumberEnum(NumberEnum.Location);
            param.setMinValue(-1.7976931348623157E308);
            param.setMaxValue(1.7976931348623157E308);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    ORIENTATION("ORIENTATION") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("ORIENTATION");
            param.setDisplayedName("Orientation");
            param.setDescription("Field of view orientation");
            param.setGroup(EnumGroup.Dimension);
            param.setCategory(Category.Acquisition);
            param.setUuid("8eb97ecf-ed84-4b47-9c39-bccb7a523157");
            param.setValue("AXIAL");
            param.setDefaultValue("AXIAL");
            param.setSuggestedValues(asList("AXIAL", "CORONAL", "SAGITTAL", "OBLIQUE"));
            param.setRestrictedToSuggested(true);
            return param;
        }
    },

    PAROPT_PARAM("PAROPT_PARAM") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("PAROPT_PARAM");
            param.setDisplayedName("Parameter optimised");
            param.setDescription("Name of the current optimised parameter");
            param.setLocked(true);
            param.setGroup(EnumGroup.Miscellaneous);
            param.setCategory(Category.Acquisition);
            param.setUuid("a417b5c9-770a-499f-bdbb-c80efddccb62");
            param.setValue("");
            param.setDefaultValue("PULSE_LENGTH");
            return param;
        }
    },

    PHASE_0("PHASE_0") {
        public Param build() {
            ListNumberParam param = new ListNumberParam();
            param.setName("PHASE_0");
            param.setDisplayedName("PHASE_0");
            param.setDescription("PHASE_0.description");
            param.setLocked(true);
            param.setGroup(EnumGroup.Miscellaneous);
            param.setCategory(Category.Process);
            param.setUuid("a85ae9bc-ef30-4790-be5d-04b9fb401c65");
            param.setMinValue(-1.7976931348623157E308);
            param.setMaxValue(1.7976931348623157E308);
            param.setNumberEnum(NumberEnum.Double);
            param.setValue(asListNumber(0.0, 0.0));
            return param;
        }
    },

    PHASE_1("PHASE_1") {
        public Param build() {
            ListNumberParam param = new ListNumberParam();
            param.setName("PHASE_1");
            param.setDisplayedName("PHASE_1");
            param.setDescription("PHASE_1.description");
            param.setLocked(true);
            param.setGroup(EnumGroup.Miscellaneous);
            param.setCategory(Category.Process);
            param.setUuid("3d42131a-4b5a-4525-b7a9-0419a05a9a54");
            param.setMinValue(-1.7976931348623157E308);
            param.setMaxValue(1.7976931348623157E308);
            param.setNumberEnum(NumberEnum.Double);
            param.setValue(asListNumber(0.0, 0.0));
            return param;
        }
    },

    PHASE_APPLIED("PHASE_APPLIED") {
        public Param build() {
            BooleanParam param = new BooleanParam();
            param.setName("PHASE_APPLIED");
            param.setDisplayedName("PHASE_APPLIED");
            param.setDescription("PHASE_APPLIED");
            param.setLocked(true);
            param.setGroup(EnumGroup.Miscellaneous);
            param.setCategory(Category.Process);
            param.setUuid("74ed0cf4-0b56-4f12-88b9-a26117af360a");
            param.setValue(false);
            param.setDefaultValue(false);
            return param;
        }
    },

    PHASE_FIELD_OF_VIEW_RATIO("PHASE_FIELD_OF_VIEW_RATIO") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("PHASE_FIELD_OF_VIEW_RATIO");
            param.setDisplayedName("Phase FOV");
            param.setDescription("The fov ratio in the phase direction");
            param.setGroup(EnumGroup.Dimension);
            param.setCategory(Category.Acquisition);
            param.setUuid("1e9eded3-fc04-4b49-9c4b-3264c0e53597");
            param.setNumberEnum(NumberEnum.PERCENT);
            param.setMinValue(0.0);
            param.setMaxValue(100.0);
            param.setValue(100.0);
            param.setDefaultValue(100.0);
            return param;
        }
    },

    PREPHASING_READ_GRADIENT_RATIO("PREPHASING_READ_GRADIENT_RATIO") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("PREPHASING_READ_GRADIENT_RATIO");
            param.setDisplayedName("Prephasing Read gradient ratio");
            param.setDescription("The prephasing reading gradient ratio");
            param.setLocked(true);
            param.setGroup(EnumGroup.Gradient);
            param.setCategory(Category.Acquisition);
            param.setUuid("cc48fcb3-4c80-4d08-9eed-fc1ce82892f7");
            param.setNumberEnum(NumberEnum.Double);
            param.setMinValue(-1.7976931348623157E308);
            param.setMaxValue(1.7976931348623157E308);
            param.setValue(0.5);
            param.setDefaultValue(2.0);
            return param;
        }
    },

    PROBE("PROBE") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("PROBE");
            param.setDisplayedName("Probe");
            param.setDescription("The probe used for the mr acquisition");
            param.setLocked(true);
            param.setGroup(EnumGroup.Miscellaneous);
            param.setCategory(Category.Acquisition);
            param.setUuid("4468555f-87c2-405b-8fe4-2e13c7346067");
            param.setValue("");
            param.setDefaultValue("");
            return param;
        }
    },

    PROBES("PROBES") {
        public Param build() {
            ListTextParam param = new ListTextParam();
            param.setName("PROBES");
            param.setDisplayedName("Probes");
            param.setDescription("The probes used for the acquisition");
            param.setLocked(true);
            param.setGroup(EnumGroup.Miscellaneous);
            param.setCategory(Category.Acquisition);
            param.setUuid("73d0b108-94ea-491a-8a8f-ac62293a1f27");
            return param;
        }
    },

    RECEIVER_COUNT("RECEIVER_COUNT") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("RECEIVER_COUNT");
            param.setDisplayedName("Receiver Count");
            param.setDescription("The number of receivers");
            param.setLocked(true);
            param.setGroup(EnumGroup.Reception);
            param.setCategory(Category.Acquisition);
            param.setUuid("0caa7dd5-799f-4092-8bc5-64a0248796ad");
            param.setNumberEnum(NumberEnum.Integer);
            param.setMinValue(1);
            param.setMaxValue(32);
            param.setValue(1);
            param.setDefaultValue(1);
            return param;
        }
    },

    RECEIVER_GAIN("RECEIVER_GAIN") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("RECEIVER_GAIN");
            param.setDisplayedName("RG");
            param.setDescription("The receiver gain");
            param.setGroup(EnumGroup.Reception);
            param.setCategory(Category.Acquisition);
            param.setUuid("f5fc784f-61a4-4f7c-9a84-5f270ca79ed5");
            param.setNumberEnum(NumberEnum.RxGain);
            param.setMinValue(0.0);
            param.setMaxValue(120.0);
            param.setValue(40.0);
            param.setDefaultValue(1.0);
            return param;
        }
    },

    REPETITION_TIME("REPETITION_TIME") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("REPETITION_TIME");
            param.setDisplayedName("TR");
            param.setDescription("The repetition time ( TR )");
            param.setGroup(EnumGroup.Delay);
            param.setCategory(Category.Acquisition);
            param.setUuid("a5e3fc86-0316-4515-a149-78ea1fb1fe55");
            param.setNumberEnum(NumberEnum.Time);
            param.setMinValue(0.0);
            param.setMaxValue(1.0E9);
            param.setValue(0.08);
            param.setDefaultValue(0.2);
            return param;
        }
    },

    RESOLUTION_3D("RESOLUTION_3D") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("RESOLUTION_3D");
            param.setDisplayedName("RESOLUTION_3D");
            param.setDescription("");
            param.setLocked(true);
            param.setGroup(EnumGroup.Scan);
            param.setCategory(Category.Acquisition);
            param.setUuid("c214751e-a3b3-4b67-9093-6164011f5276");
            param.setNumberEnum(NumberEnum.Length);
            param.setMinValue(0.0);
            param.setMaxValue(1.7976931348623157E308);
            param.setValue(0.0625);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    RF_SPOILING_INCREMENT("RF_SPOILING_INCREMENT") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("RF_SPOILING_INCREMENT");
            param.setDisplayedName("RF_SPOILING_INCREMENT");
            param.setDescription("Quadratic RF phase-increment");
            param.setGroup(EnumGroup.Scan);
            param.setCategory(Category.Acquisition);
            param.setUuid("9def5f58-cca2-4341-b286-577860aa8ef7");
            param.setNumberEnum(NumberEnum.Angle);
            param.setMinValue(0.0);
            param.setMaxValue(360.0);
            param.setValue(117.0);
            param.setDefaultValue(117.0);
            return param;
        }
    },

    SEQUENCE_NAME("SEQUENCE_NAME") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("SEQUENCE_NAME");
            param.setDisplayedName("Seq");
            param.setDescription("The name of the sequence");
            param.setLocked(true);
            param.setLockedToDefault(true);
            param.setGroup(EnumGroup.Miscellaneous);
            param.setCategory(Category.Acquisition);
            param.setUuid("01e46ffe-815e-4916-a018-69ce65cbbb69");
            param.setValue("GRADIENT_ECHO_3D");
            param.setDefaultValue("GRADIENT_ECHO_3D");
            return param;
        }
    },

    SEQUENCE_TIME("SEQUENCE_TIME") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("SEQUENCE_TIME");
            param.setDisplayedName("SEQUENCE_TIME");
            param.setDescription("");
            param.setCategory(Category.Acquisition);
            param.setUuid("dc2d6b86-8bf8-425a-a8cd-138267a9e7b9");
            param.setNumberEnum(NumberEnum.Time);
            param.setMinValue(0.0);
            param.setMaxValue(1.0E9);
            param.setValue(25.6);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    SEQUENCE_VERSION("SEQUENCE_VERSION") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("SEQUENCE_VERSION");
            param.setDisplayedName("SEQUENCE_VERSION");
            param.setDescription("");
            param.setGroup(EnumGroup.User);
            param.setCategory(Category.Acquisition);
            param.setUuid("c59213fe-0279-4336-899e-b81d6cb04151");
            param.setValue("base");
            param.setDefaultValue("");
            return param;
        }
    },

    SETUP_MODE("SETUP_MODE") {
        public Param build() {
            BooleanParam param = new BooleanParam();
            param.setName("SETUP_MODE");
            param.setDisplayedName("Setup");
            param.setDescription("True during setup process");
            param.setLocked(true);
            param.setGroup(EnumGroup.Miscellaneous);
            param.setCategory(Category.Acquisition);
            param.setUuid("8630886e-7721-4ae4-aac9-a086e0a14c77");
            param.setValue(false);
            param.setDefaultValue(false);
            return param;
        }
    },

    SLICE_REFOCUSING_GRADIENT_RATIO("SLICE_REFOCUSING_GRADIENT_RATIO") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("SLICE_REFOCUSING_GRADIENT_RATIO");
            param.setDisplayedName("Ratio of the refocusing gradient");
            param.setDescription("The ratio of the slice refocusing gradient");
            param.setLocked(true);
            param.setGroup(EnumGroup.Gradient);
            param.setCategory(Category.Acquisition);
            param.setUuid("2f8d7a76-4f50-442d-b13c-5d442338d722");
            param.setNumberEnum(NumberEnum.Double);
            param.setMinValue(-1.7976931348623157E308);
            param.setMaxValue(1.7976931348623157E308);
            param.setValue(0.5);
            param.setDefaultValue(2.0);
            return param;
        }
    },

    SLICE_THICKNESS("SLICE_THICKNESS") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("SLICE_THICKNESS");
            param.setDisplayedName("SL");
            param.setDescription("The slice thickness");
            param.setGroup(EnumGroup.Dimension);
            param.setCategory(Category.Acquisition);
            param.setUuid("6a4e7ef8-a6ef-4804-8b90-c1444ba43e11");
            param.setNumberEnum(NumberEnum.Length);
            param.setMinValue(5.0E-5);
            param.setMaxValue(1.7976931348623157E308);
            param.setValue(0.002);
            param.setDefaultValue(0.005);
            return param;
        }
    },

    SOFTWARE_VERSION("SOFTWARE_VERSION") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("SOFTWARE_VERSION");
            param.setDisplayedName("SOFTWARE_VERSION");
            param.setDescription("The version of the software");
            param.setLocked(true);
            param.setGroup(EnumGroup.Miscellaneous);
            param.setCategory(Category.Miscellaneous);
            param.setUuid("5ff631a7-b02d-492b-86a7-bb5944fd8994");
            param.setValue("Software version");
            param.setDefaultValue("Software version");
            return param;
        }
    },

    SPACING_BETWEEN_SLICE("SPACING_BETWEEN_SLICE") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("SPACING_BETWEEN_SLICE");
            param.setDisplayedName("Slice Spacing");
            param.setDescription("Spacing betwin slice");
            param.setGroup(EnumGroup.Dimension);
            param.setCategory(Category.Acquisition);
            param.setUuid("325579ca-7f05-4dcf-97a6-935b440aea74");
            param.setNumberEnum(NumberEnum.Length);
            param.setMinValue(0.0);
            param.setMaxValue(1.7976931348623157E308);
            param.setValue(0.004);
            param.setDefaultValue(5.0);
            return param;
        }
    },

    SPECTRAL_WIDTH("SPECTRAL_WIDTH") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("SPECTRAL_WIDTH");
            param.setDisplayedName("SW");
            param.setDescription("The spectral width of the reception ( SW BW, bandwidth )");
            param.setGroup(EnumGroup.Reception);
            param.setCategory(Category.Acquisition);
            param.setUuid("7a66253b-8871-400a-9e78-4bb9c3fc14f6");
            param.setNumberEnum(NumberEnum.SW);
            param.setMinValue(0.0);
            param.setMaxValue(1.0E8);
            param.setValue(20001.280081925244);
            param.setDefaultValue(12500.0);
            return param;
        }
    },

    STATION_NAME("STATION_NAME") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("STATION_NAME");
            param.setDisplayedName("STATION_NAME");
            param.setDescription("Station name");
            param.setLocked(true);
            param.setGroup(EnumGroup.Miscellaneous);
            param.setCategory(Category.Miscellaneous);
            param.setUuid("ebf0c22b-19da-4716-9116-1af201eaa280");
            param.setValue("Station name");
            param.setDefaultValue("Station name");
            return param;
        }
    },

    SUBJECT_POSITION("SUBJECT_POSITION") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("SUBJECT_POSITION");
            param.setDisplayedName("SUBJECT_POSITION");
            param.setDescription("Subject position relative to the magnet.");
            param.setLocked(true);
            param.setGroup(EnumGroup.Dimension);
            param.setCategory(Category.Acquisition);
            param.setUuid("ca42d229-467d-4ef9-8495-02da0b0c3323");
            param.setValue("HeadFirstProne");
            param.setDefaultValue("FeetFirstProne");
            param.setSuggestedValues(asList("HeadFirstProne", "HeadFirstSupine", "FeetFirstProne", "FeetFirstSupine"));
            param.setRestrictedToSuggested(true);
            return param;
        }
    },

    TRANSFORM_PLUGIN("TRANSFORM_PLUGIN") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("TRANSFORM_PLUGIN");
            param.setDisplayedName("Transform plugin");
            param.setDescription("Transform the acquisition space to the k space");
            param.setGroup(EnumGroup.Scan);
            param.setCategory(Category.Acquisition);
            param.setUuid("32b5829c-e276-4709-b965-d37de9143ed2");
            param.setValue("Sequential4D");
            param.setDefaultValue("Sequential4D");
            param.setSuggestedValues(asList("Sequential4D"));
            return param;
        }
    },

    TRANSMIT_FREQ_1("TRANSMIT_FREQ_1") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("TRANSMIT_FREQ_1");
            param.setDisplayedName("Transmit Freq 1");
            param.setDescription("Transmit frequency of the first sequence channel");
            param.setLocked(true);
            param.setGroup(EnumGroup.Emission);
            param.setCategory(Category.Acquisition);
            param.setUuid("0b6f803e-b660-4785-96b2-4665d84daed9");
            param.setNumberEnum(NumberEnum.Frequency);
            param.setMinValue(0.0);
            param.setMaxValue(3.0E9);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    TRANSMIT_FREQ_2("TRANSMIT_FREQ_2") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("TRANSMIT_FREQ_2");
            param.setDisplayedName("Transmit Freq 2");
            param.setDescription("Transmit frequency of the second sequence channel");
            param.setLocked(true);
            param.setGroup(EnumGroup.Emission);
            param.setCategory(Category.Acquisition);
            param.setUuid("de55d6cd-c225-4a66-9642-4f8189cea617");
            param.setNumberEnum(NumberEnum.Frequency);
            param.setMinValue(0.0);
            param.setMaxValue(3.0E9);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    TRANSMIT_FREQ_3("TRANSMIT_FREQ_3") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("TRANSMIT_FREQ_3");
            param.setDisplayedName("Transmit Freq 3");
            param.setDescription("Transmit frequency of the third sequence channel");
            param.setLocked(true);
            param.setGroup(EnumGroup.Emission);
            param.setCategory(Category.Acquisition);
            param.setUuid("fd6132f8-7cb6-4987-abc7-b7c70e142988");
            param.setNumberEnum(NumberEnum.Frequency);
            param.setMinValue(0.0);
            param.setMaxValue(3.0E9);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    TRANSMIT_FREQ_4("TRANSMIT_FREQ_4") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("TRANSMIT_FREQ_4");
            param.setDisplayedName("Transmit Freq 4");
            param.setDescription("Transmit frequency of the fourth sequence channel");
            param.setLocked(true);
            param.setGroup(EnumGroup.Emission);
            param.setCategory(Category.Acquisition);
            param.setUuid("1c8778a5-6cf6-4042-a36e-eb3b4e600479");
            param.setNumberEnum(NumberEnum.Frequency);
            param.setMinValue(0.0);
            param.setMaxValue(3.0E9);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    TX_AMP_90("TX_AMP_90") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("TX_AMP_90");
            param.setDisplayedName("TX_AMP");
            param.setDescription("Amplitude of the transmitter");
            param.setCategory(Category.Acquisition);
            param.setUuid("1e79a1e4-850b-48d6-a4d4-fc81ace0dbb9");
            param.setNumberEnum(NumberEnum.TxAmp);
            param.setMinValue(0.0);
            param.setMaxValue(100.0);
            param.setValue(20.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    TX_AMP_ATT_AUTO("TX_AMP_ATT_AUTO") {
        public Param build() {
            BooleanParam param = new BooleanParam();
            param.setName("TX_AMP_ATT_AUTO");
            param.setDisplayedName("TX_AMP_ATT_AUTO");
            param.setDescription("");
            param.setCategory(Category.Acquisition);
            param.setUuid("f88b0bbf-9d23-4fe4-a142-1287fba001d5");
            param.setValue(false);
            param.setDefaultValue(true);
            return param;
        }
    },

    TX_ATT("TX_ATT") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("TX_ATT");
            param.setDisplayedName("TX_ATT");
            param.setDescription("Emission attenuation");
            param.setGroup(EnumGroup.Emission);
            param.setCategory(Category.Acquisition);
            param.setUuid("af42f5be-ffe5-44e4-a745-c298f69ab23f");
            param.setNumberEnum(NumberEnum.TxAtt);
            param.setMinValue(0);
            param.setMaxValue(63);
            param.setValue(45);
            param.setDefaultValue(36);
            return param;
        }
    },

    TX_BANDWIDTH_FACTOR_3D("TX_BANDWIDTH_FACTOR_3D") {
        public Param build() {
            ListNumberParam param = new ListNumberParam();
            param.setName("TX_BANDWIDTH_FACTOR_3D");
            param.setDisplayedName("TX_BANDWIDTH_FACTOR_3D");
            param.setDescription("Bandwidth factor 3D for pulse shapes: 1-HARD, 2-GAUSSIAN, 3-SINC3, 4-SINC5");
            param.setLocked(true);
            param.setGroup(EnumGroup.Emission);
            param.setCategory(Category.Acquisition);
            param.setUuid("87ec7daa-f592-4f51-8c3f-cb5061171c5a");
            param.setMinValue(-1.7976931348623157E308);
            param.setMaxValue(1.7976931348623157E308);
            param.setNumberEnum(NumberEnum.Double);
            param.setValue(asListNumber(1.1, 3.2, 5.0, 7.3));
            param.setDefaultValue(asListNumber(1.1, 3.2, 5.0, 7.3));
            return param;
        }
    },

    TX_LENGTH("TX_LENGTH") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("TX_LENGTH");
            param.setDisplayedName("TX_LENGTH");
            param.setDescription("length of RF pulse");
            param.setGroup(EnumGroup.Emission);
            param.setCategory(Category.Acquisition);
            param.setUuid("f06c5813-a16e-4157-b402-fd29af4ceb42");
            param.setNumberEnum(NumberEnum.Time);
            param.setMinValue(0.0);
            param.setMaxValue(1.0E9);
            param.setValue(5.0E-4);
            param.setDefaultValue(0.001);
            return param;
        }
    },

    TX_ROUTE("TX_ROUTE") {
        public Param build() {
            ListNumberParam param = new ListNumberParam();
            param.setName("TX_ROUTE");
            param.setDisplayedName("TX_ROUTE");
            param.setDescription("LogCh->PhysCh");
            param.setLocked(true);
            param.setGroup(EnumGroup.Emission);
            param.setCategory(Category.Acquisition);
            param.setUuid("c59eb806-5367-485d-bf1e-cd51674aa4ef");
            param.setMinValue(-2147483648);
            param.setMaxValue(2147483647);
            param.setNumberEnum(NumberEnum.Integer);
            param.setValue(asListNumber(0));
            return param;
        }
    },

    TX_SHAPE("TX_SHAPE") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("TX_SHAPE");
            param.setDisplayedName("TX_SHAPE");
            param.setDescription("the shape of the rf pulse");
            param.setLocked(true);
            param.setGroup(EnumGroup.Emission);
            param.setCategory(Category.Acquisition);
            param.setUuid("ba7bdda5-ec04-4b72-9feb-9891af1f354d");
            param.setValue("GAUSSIAN");
            param.setDefaultValue("GAUSSIAN");
            param.setSuggestedValues(asList("HARD", "GAUSSIAN", "SINC3", "SINC5", "SLR_8_5152", "SLR_4_2576"));
            return param;
        }
    },

    USER_MATRIX_DIMENSION_1D("USER_MATRIX_DIMENSION_1D") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("USER_MATRIX_DIMENSION_1D");
            param.setDisplayedName("USER_ACQUISITION_MATRIX_DIMENSION_1D");
            param.setDescription("");
            param.setGroup(EnumGroup.User);
            param.setCategory(Category.Acquisition);
            param.setUuid("01395a80-2c9d-4d56-9ee1-997add782b1f");
            param.setNumberEnum(NumberEnum.Scan);
            param.setMinValue(1);
            param.setMaxValue(65536);
            param.setValue(81);
            param.setDefaultValue(1);
            return param;
        }
    },

    USER_MATRIX_DIMENSION_2D("USER_MATRIX_DIMENSION_2D") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("USER_MATRIX_DIMENSION_2D");
            param.setDisplayedName("USER_ACQUISITION_MATRIX_DIMENSION_2D");
            param.setDescription("");
            param.setGroup(EnumGroup.User);
            param.setCategory(Category.Acquisition);
            param.setUuid("ec135937-3a56-44d5-81ce-2b38c7b01c52");
            param.setNumberEnum(NumberEnum.Scan);
            param.setMinValue(1);
            param.setMaxValue(65536);
            param.setValue(81);
            param.setDefaultValue(1);
            return param;
        }
    },

    USER_MATRIX_DIMENSION_3D("USER_MATRIX_DIMENSION_3D") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("USER_MATRIX_DIMENSION_3D");
            param.setDisplayedName("USER_ACQUISITION_MATRIX_DIMENSION_3D");
            param.setDescription("");
            param.setGroup(EnumGroup.User);
            param.setCategory(Category.Acquisition);
            param.setUuid("7643226a-4afe-48ea-ab36-74f33de9bea6");
            param.setNumberEnum(NumberEnum.Scan);
            param.setMinValue(1);
            param.setMaxValue(65536);
            param.setValue(4);
            param.setDefaultValue(1);
            return param;
        }
    },

    USER_MATRIX_DIMENSION_4D("USER_MATRIX_DIMENSION_4D") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("USER_MATRIX_DIMENSION_4D");
            param.setDisplayedName("USER_ACQUISITION_MATRIX_DIMENSION_4D");
            param.setDescription("");
            param.setGroup(EnumGroup.User);
            param.setCategory(Category.Acquisition);
            param.setUuid("0a4c5242-27ab-45d9-a137-f15e82fec496");
            param.setNumberEnum(NumberEnum.Scan);
            param.setMinValue(1);
            param.setMaxValue(65536);
            param.setValue(1);
            param.setDefaultValue(1);
            return param;
        }
    },

    USER_TMP_PARAM_BOOL_1("USER_TMP_PARAM_BOOL_1") {
        public Param build() {
            BooleanParam param = new BooleanParam();
            param.setName("USER_TMP_PARAM_BOOL_1");
            param.setDisplayedName("USER_TMP_PARAM_BOOL_1");
            param.setDescription("");
            param.setLocked(true);
            param.setCategory(Category.Acquisition);
            param.setUuid("8396db97-a865-4232-836b-17a93b62d5b3");
            param.setValue(false);
            param.setDefaultValue(false);
            return param;
        }
    },

    USER_TMP_PARAM_BOOL_2("USER_TMP_PARAM_BOOL_2") {
        public Param build() {
            BooleanParam param = new BooleanParam();
            param.setName("USER_TMP_PARAM_BOOL_2");
            param.setDisplayedName("USER_TMP_PARAM_BOOL_2");
            param.setDescription("");
            param.setLocked(true);
            param.setCategory(Category.Acquisition);
            param.setUuid("ef9ec97a-2da4-4c38-890b-bccc3e10fe46");
            param.setValue(false);
            param.setDefaultValue(false);
            return param;
        }
    },

    USER_TMP_PARAM_LIST_1("USER_TMP_PARAM_LIST_1") {
        public Param build() {
            ListNumberParam param = new ListNumberParam();
            param.setName("USER_TMP_PARAM_LIST_1");
            param.setDisplayedName("USER_TMP_PARAM_LIST_1");
            param.setDescription("");
            param.setLocked(true);
            param.setCategory(Category.Acquisition);
            param.setUuid("dd9fcfee-9eed-4c86-aefa-80faf86cc88f");
            param.setMinValue(-1.7976931348623157E308);
            param.setMaxValue(1.7976931348623157E308);
            param.setNumberEnum(NumberEnum.Double);
            return param;
        }
    },

    USER_TMP_PARAM_LIST_2("USER_TMP_PARAM_LIST_2") {
        public Param build() {
            ListNumberParam param = new ListNumberParam();
            param.setName("USER_TMP_PARAM_LIST_2");
            param.setDisplayedName("USER_TMP_PARAM_LIST_2");
            param.setDescription("");
            param.setLocked(true);
            param.setCategory(Category.Acquisition);
            param.setUuid("2998ac4a-da15-4e50-ba6f-0d5d55e63587");
            param.setMinValue(-1.7976931348623157E308);
            param.setMaxValue(1.7976931348623157E308);
            param.setNumberEnum(NumberEnum.Double);
            return param;
        }
    },

    USER_TMP_PARAM_NUM_1("USER_TMP_PARAM_NUM_1") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("USER_TMP_PARAM_NUM_1");
            param.setDisplayedName("USER_TMP_PARAM_NUM_1");
            param.setDescription("");
            param.setLocked(true);
            param.setCategory(Category.Acquisition);
            param.setUuid("72dbcf67-7712-4f47-a930-050d4d0f4002");
            param.setNumberEnum(NumberEnum.Double);
            param.setMinValue(-1.7976931348623157E308);
            param.setMaxValue(1.7976931348623157E308);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    USER_TMP_PARAM_NUM_2("USER_TMP_PARAM_NUM_2") {
        public Param build() {
            NumberParam param = new NumberParam();
            param.setName("USER_TMP_PARAM_NUM_2");
            param.setDisplayedName("USER_TMP_PARAM_NUM_2");
            param.setDescription("");
            param.setLocked(true);
            param.setCategory(Category.Acquisition);
            param.setUuid("1f620e0e-b7ac-459b-a2d4-99e9e06300c8");
            param.setNumberEnum(NumberEnum.Double);
            param.setMinValue(-1.7976931348623157E308);
            param.setMaxValue(1.7976931348623157E308);
            param.setValue(0.0);
            param.setDefaultValue(0.0);
            return param;
        }
    },

    USER_TMP_PARAM_STR_1("USER_TMP_PARAM_STR_1") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("USER_TMP_PARAM_STR_1");
            param.setDisplayedName("USER_TMP_PARAM_STR_1");
            param.setDescription("");
            param.setLocked(true);
            param.setCategory(Category.Acquisition);
            param.setUuid("caaabdf3-70d4-4d62-82dd-b9753f294c8a");
            param.setValue("");
            param.setDefaultValue("");
            return param;
        }
    },

    USER_TMP_PARAM_STR_2("USER_TMP_PARAM_STR_2") {
        public Param build() {
            TextParam param = new TextParam();
            param.setName("USER_TMP_PARAM_STR_2");
            param.setDisplayedName("USER_TMP_PARAM_STR_2");
            param.setDescription("");
            param.setLocked(true);
            param.setCategory(Category.Acquisition);
            param.setUuid("8966748e-5eec-4b6e-9df1-599e2c9a7a91");
            param.setValue("");
            param.setDefaultValue("");
            return param;
        }
    };

    //--

    private final String name;

    private U(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    //--

    private static List<Number> asListNumber(Number ... numbers) {
        return asList(numbers);
    }
}
