<?xml version="1.0" encoding="UTF-8"?>

<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
<!--                                                                       -->
<!--                   This file is part of the project                    -->
<!--            NOPT - gas transport Network OPTimizer                     -->
<!--                                                                       -->
<!--     written by  Martin Schmidt                                        -->
<!--  Copyright (C)  2010 LUH                                              -->
<!--                                                                       -->
<!--  Distribution only among partners according to project agreements.    -->
<!--  Any questions should be sent to nopt@zib.de .                        -->
<!--                                                                       -->
<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

<compressorStations xmlns="http://ifam.uni-hannover.de/ao/nopt/compressorStation"
	xmlns:framework="http://mathematik.tu-darmstadt.de/opt/framework/XMLSchema"
	xmlns:gas="http://mathematik.tu-darmstadt.de/opt/gas/XMLSchema"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://ifam.uni-hannover.de/ao/nopt/compressorStation
	../../../schema/CompressorStations.xsd">

  <compressorStation id="eN_KRUH_VS">
    <compressors>
      <turboCompressor drive="P_M3002" id="T_KRUH_M3">
        <speedMin value="4700" unit="per_min"/>
        <speedMax value="6500" unit="per_min"/>
        <n_isoline_coeff_1 value="259.194"/>
        <n_isoline_coeff_2 value="-0.0865313"/>
        <n_isoline_coeff_3 value="7.96321e-06"/>
        <n_isoline_coeff_4 value="-257.938"/>
        <n_isoline_coeff_5 value="0.0946806"/>
        <n_isoline_coeff_6 value="-7.14148e-06"/>
        <n_isoline_coeff_7 value="40.343"/>
        <n_isoline_coeff_8 value="-0.0179589"/>
        <n_isoline_coeff_9 value="1.48827e-06"/>
        <eta_ad_isoline_coeff_1 value="4.86605"/>
        <eta_ad_isoline_coeff_2 value="-0.00129653"/>
        <eta_ad_isoline_coeff_3 value="5.58797e-08"/>
        <eta_ad_isoline_coeff_4 value="-3.61075"/>
        <eta_ad_isoline_coeff_5 value="0.00147876"/>
        <eta_ad_isoline_coeff_6 value="-1.0918e-07"/>
        <eta_ad_isoline_coeff_7 value="0.142"/>
        <eta_ad_isoline_coeff_8 value="-0.000159514"/>
        <eta_ad_isoline_coeff_9 value="1.55149e-08"/>
        <surgeline_coeff_1 value="-37.8408"/>
        <surgeline_coeff_2 value="48.2651"/>
        <surgeline_coeff_3 value="0.776384"/>
        <chokeline_coeff_1 value="-9.35445"/>
        <chokeline_coeff_2 value="8.71219"/>
        <chokeline_coeff_3 value="0.707302"/>
        <efficiencyOfChokeline value="0.5"/>
        <settlelineMeasurements>
          <measurement>
            <speed value="4550" unit="per_min"/>
            <adiabaticHead value="42.96" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.63" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="4900" unit="per_min"/>
            <adiabaticHead value="49.94" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.77" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="5200" unit="per_min"/>
            <adiabaticHead value="57.08" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.91" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="5500" unit="per_min"/>
            <adiabaticHead value="63.98" unit="kJ_per_kg"/>
            <volumetricFlowrate value="2.04" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="5850" unit="per_min"/>
            <adiabaticHead value="72.1" unit="kJ_per_kg"/>
            <volumetricFlowrate value="2.2" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="6500" unit="per_min"/>
            <adiabaticHead value="88.18" unit="kJ_per_kg"/>
            <volumetricFlowrate value="2.51" unit="m_cube_per_s"/>
          </measurement>
        </settlelineMeasurements>
        <characteristicDiagramMeasurements>
          <adiabaticEfficiency value="0.82">
            <measurement>
              <speed value="4550" unit="per_min"/>
              <adiabaticHead value="42.27" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.7" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4900" unit="per_min"/>
              <adiabaticHead value="49.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.86" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5200" unit="per_min"/>
              <adiabaticHead value="56.03" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.02" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5500" unit="per_min"/>
              <adiabaticHead value="62.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.17" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5850" unit="per_min"/>
              <adiabaticHead value="70.29" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.35" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="6500" unit="per_min"/>
              <adiabaticHead value="85.37" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.7" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.8">
            <measurement>
              <speed value="4550" unit="per_min"/>
              <adiabaticHead value="39.56" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.92" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4900" unit="per_min"/>
              <adiabaticHead value="45.13" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.16" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5200" unit="per_min"/>
              <adiabaticHead value="51.04" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.36" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5500" unit="per_min"/>
              <adiabaticHead value="56.87" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.52" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5850" unit="per_min"/>
              <adiabaticHead value="63.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.7" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="6500" unit="per_min"/>
              <adiabaticHead value="77.92" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.04" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.75">
            <measurement>
              <speed value="4550" unit="per_min"/>
              <adiabaticHead value="35.27" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.16" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4900" unit="per_min"/>
              <adiabaticHead value="40.38" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.4" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5200" unit="per_min"/>
              <adiabaticHead value="45.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.6" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5500" unit="per_min"/>
              <adiabaticHead value="50.48" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.78" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5850" unit="per_min"/>
              <adiabaticHead value="56.13" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.99" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="6500" unit="per_min"/>
              <adiabaticHead value="67.69" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.37" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.7">
            <measurement>
              <speed value="4550" unit="per_min"/>
              <adiabaticHead value="31.42" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.33" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4900" unit="per_min"/>
              <adiabaticHead value="35.91" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.58" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5200" unit="per_min"/>
              <adiabaticHead value="40.43" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.79" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5500" unit="per_min"/>
              <adiabaticHead value="44.36" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.98" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5850" unit="per_min"/>
              <adiabaticHead value="49.29" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.2" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="6500" unit="per_min"/>
              <adiabaticHead value="58.83" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.6" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.65">
            <measurement>
              <speed value="4550" unit="per_min"/>
              <adiabaticHead value="28.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.45" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4900" unit="per_min"/>
              <adiabaticHead value="32.49" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.7" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5200" unit="per_min"/>
              <adiabaticHead value="36.29" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.92" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5500" unit="per_min"/>
              <adiabaticHead value="39.45" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.12" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5850" unit="per_min"/>
              <adiabaticHead value="43.78" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.34" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="6500" unit="per_min"/>
              <adiabaticHead value="52.29" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.75" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.6">
            <measurement>
              <speed value="4550" unit="per_min"/>
              <adiabaticHead value="25.44" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.55" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4900" unit="per_min"/>
              <adiabaticHead value="29.06" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.81" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5200" unit="per_min"/>
              <adiabaticHead value="32.27" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.04" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5500" unit="per_min"/>
              <adiabaticHead value="35.23" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.23" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5850" unit="per_min"/>
              <adiabaticHead value="38.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.46" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="6500" unit="per_min"/>
              <adiabaticHead value="46.13" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.88" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.5">
            <measurement>
              <speed value="4550" unit="per_min"/>
              <adiabaticHead value="20.38" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.71" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4900" unit="per_min"/>
              <adiabaticHead value="23.55" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.97" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5200" unit="per_min"/>
              <adiabaticHead value="26.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.19" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5500" unit="per_min"/>
              <adiabaticHead value="28.96" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.38" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5850" unit="per_min"/>
              <adiabaticHead value="32.51" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.6" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="6500" unit="per_min"/>
              <adiabaticHead value="39.51" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.01" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
        </characteristicDiagramMeasurements>
      </turboCompressor>
<!-- Leistungsstaerkste Maschine
      <turboCompressor drive="P_RB211_a" id="T_KRUH_M4">
        <speedMin value="3500" unit="per_min"/>
        <speedMax value="4250" unit="per_min"/>
        <n_isoline_coeff_1 value="-152.782"/>
        <n_isoline_coeff_2 value="0.0747259"/>
        <n_isoline_coeff_3 value="-1.08257e-05"/>
        <n_isoline_coeff_4 value="49.6398"/>
        <n_isoline_coeff_5 value="-0.011331"/>
        <n_isoline_coeff_6 value="2.46697e-06"/>
        <n_isoline_coeff_7 value="-12.1504"/>
        <n_isoline_coeff_8 value="0.00245011"/>
        <n_isoline_coeff_9 value="-2.33309e-07"/>
        <eta_ad_isoline_coeff_1 value="1.78281"/>
        <eta_ad_isoline_coeff_2 value="-0.000475987"/>
        <eta_ad_isoline_coeff_3 value="1.20675e-08"/>
        <eta_ad_isoline_coeff_4 value="-0.33333"/>
        <eta_ad_isoline_coeff_5 value="0.000288209"/>
        <eta_ad_isoline_coeff_6 value="-2.8034e-08"/>
        <eta_ad_isoline_coeff_7 value="-0.0707588"/>
        <eta_ad_isoline_coeff_8 value="2.47539e-06"/>
        <eta_ad_isoline_coeff_9 value="8.1193e-10"/>
        <surgeline_coeff_1 value="-27.9847"/>
        <surgeline_coeff_2 value="29.1404"/>
        <surgeline_coeff_3 value="0.648166"/>
        <chokeline_coeff_1 value="-15.1033"/>
        <chokeline_coeff_2 value="7.6154"/>
        <chokeline_coeff_3 value="0.364426"/>
        <efficiencyOfChokeline value="0.7"/>
        <settlelineMeasurements>
          <measurement>
            <speed value="3500" unit="per_min"/>
            <adiabaticHead value="42.3" unit="kJ_per_kg"/>
            <volumetricFlowrate value="2.3" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="3750" unit="per_min"/>
            <adiabaticHead value="49" unit="kJ_per_kg"/>
            <volumetricFlowrate value="2.5" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="4000" unit="per_min"/>
            <adiabaticHead value="55.5" unit="kJ_per_kg"/>
            <volumetricFlowrate value="2.7" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="4250" unit="per_min"/>
            <adiabaticHead value="63.3" unit="kJ_per_kg"/>
            <volumetricFlowrate value="2.93" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="4500" unit="per_min"/>
            <adiabaticHead value="71" unit="kJ_per_kg"/>
            <volumetricFlowrate value="3.18" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="4725" unit="per_min"/>
            <adiabaticHead value="77.8" unit="kJ_per_kg"/>
            <volumetricFlowrate value="3.38" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="4950" unit="per_min"/>
            <adiabaticHead value="84.5" unit="kJ_per_kg"/>
            <volumetricFlowrate value="3.58" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="5200" unit="per_min"/>
            <adiabaticHead value="95" unit="kJ_per_kg"/>
            <volumetricFlowrate value="3.88" unit="m_cube_per_s"/>
          </measurement>
        </settlelineMeasurements>
        <characteristicDiagramMeasurements>
          <adiabaticEfficiency value="0.79">
            <measurement>
              <speed value="3500" unit="per_min"/>
              <adiabaticHead value="38.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.4" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="3750" unit="per_min"/>
              <adiabaticHead value="44.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.68" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4000" unit="per_min"/>
              <adiabaticHead value="51" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.93" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4250" unit="per_min"/>
              <adiabaticHead value="57.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.2" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4500" unit="per_min"/>
              <adiabaticHead value="64.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.47" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4725" unit="per_min"/>
              <adiabaticHead value="71.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.75" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4950" unit="per_min"/>
              <adiabaticHead value="78.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.05" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5200" unit="per_min"/>
              <adiabaticHead value="86.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.38" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.76">
            <measurement>
              <speed value="3500" unit="per_min"/>
              <adiabaticHead value="33.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="3750" unit="per_min"/>
              <adiabaticHead value="39" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.38" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4000" unit="per_min"/>
              <adiabaticHead value="44" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.72" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4250" unit="per_min"/>
              <adiabaticHead value="49.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.07" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4500" unit="per_min"/>
              <adiabaticHead value="55.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.45" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4725" unit="per_min"/>
              <adiabaticHead value="61.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.82" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4950" unit="per_min"/>
              <adiabaticHead value="67" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.13" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5200" unit="per_min"/>
              <adiabaticHead value="74.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.5" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.7">
            <measurement>
              <speed value="3500" unit="per_min"/>
              <adiabaticHead value="27" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.47" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="3750" unit="per_min"/>
              <adiabaticHead value="31.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.88" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4000" unit="per_min"/>
              <adiabaticHead value="35" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.25" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4250" unit="per_min"/>
              <adiabaticHead value="39.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.65" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4500" unit="per_min"/>
              <adiabaticHead value="43.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.03" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4725" unit="per_min"/>
              <adiabaticHead value="48.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.45" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="4950" unit="per_min"/>
              <adiabaticHead value="53.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.8" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5200" unit="per_min"/>
              <adiabaticHead value="59" unit="kJ_per_kg"/>
              <volumetricFlowrate value="7.25" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
        </characteristicDiagramMeasurements>
      </turboCompressor>
-->
<!-- ending of the drives id renamed to avoid naming conflicts -->
      <pistonCompressor drive="M_16Z330_a" id="K_KRUH_M1">
        <speedMin value="165" unit="per_min"/>
        <speedMax value="310" unit="per_min"/>
        <operatingVolume value="0.5" unit="m_cube"/>
        <maximalTorque value="0" unit="kNm"/>
        <maximalCompressionRatio value="2"/>
        <adiabaticEfficiency value="0.9"/>
        <additionalReductionVolFlow value="0.34"/>
      </pistonCompressor>
<!-- ending of the drives id renamed to avoid naming conflicts -->
      <pistonCompressor drive="M_16Z330_b" id="K_KRUH_M2">
        <speedMin value="165" unit="per_min"/>
        <speedMax value="310" unit="per_min"/>
        <operatingVolume value="0.5" unit="m_cube"/>
        <maximalTorque value="0" unit="kNm"/>
        <maximalCompressionRatio value="2"/>
        <adiabaticEfficiency value="0.9"/>
        <additionalReductionVolFlow value="0.34"/>
      </pistonCompressor>
    </compressors>
    <drives>
      <gasTurbine id="P_M3002">
        <energy_rate_fun_coeff_1 value="9998.93"/>
        <energy_rate_fun_coeff_2 value="2.80027"/>
        <energy_rate_fun_coeff_3 value="-1.97177e-08"/>
        <power_fun_coeff_1 value="206.88"/>
        <power_fun_coeff_2 value="3.29868"/>
        <power_fun_coeff_3 value="-0.000245917"/>
        <power_fun_coeff_4 value="-3.043"/>
        <power_fun_coeff_5 value="-0.0196877"/>
        <power_fun_coeff_6 value="1.45562e-06"/>
        <power_fun_coeff_7 value="0.052"/>
        <power_fun_coeff_8 value="4.61538e-06"/>
        <power_fun_coeff_9 value="1.91299e-20"/>
        <specificEnergyConsumptionMeasurements>
          <measurement>
            <compressorPower value="9738" unit="kW"/>
            <fuelConsumption value="37266" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="9430" unit="kW"/>
            <fuelConsumption value="36404" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="8098" unit="kW"/>
            <fuelConsumption value="32674" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="7585" unit="kW"/>
            <fuelConsumption value="31238" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="6560" unit="kW"/>
            <fuelConsumption value="28368" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="6304" unit="kW"/>
            <fuelConsumption value="27651" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="4920" unit="kW"/>
            <fuelConsumption value="23776" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="4818" unit="kW"/>
            <fuelConsumption value="23490" unit="kW"/>
          </measurement>
        </specificEnergyConsumptionMeasurements>
        <maximalPowerMeasurements>
          <ambientTemperature value="-5" unit="C">
            <measurement>
              <speed value="4550" unit="per_min"/>
              <maximalPower value="10439" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="5200" unit="per_min"/>
              <maximalPower value="11043" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="5850" unit="per_min"/>
              <maximalPower value="11432" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="6500" unit="per_min"/>
              <maximalPower value="11608" unit="kW"/>
            </measurement>
          </ambientTemperature>
          <ambientTemperature value="5" unit="C">
            <measurement>
              <speed value="4550" unit="per_min"/>
              <maximalPower value="9814" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="5200" unit="per_min"/>
              <maximalPower value="10382" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="5850" unit="per_min"/>
              <maximalPower value="10748" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="6500" unit="per_min"/>
              <maximalPower value="10913" unit="kW"/>
            </measurement>
          </ambientTemperature>
          <ambientTemperature value="15" unit="C">
            <measurement>
              <speed value="4550" unit="per_min"/>
              <maximalPower value="9204" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="5200" unit="per_min"/>
              <maximalPower value="9737" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="5850" unit="per_min"/>
              <maximalPower value="10080" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="6500" unit="per_min"/>
              <maximalPower value="10234" unit="kW"/>
            </measurement>
          </ambientTemperature>
          <ambientTemperature value="25" unit="C">
            <measurement>
              <speed value="4550" unit="per_min"/>
              <maximalPower value="8608" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="5200" unit="per_min"/>
              <maximalPower value="9107" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="5850" unit="per_min"/>
              <maximalPower value="9427" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="6500" unit="per_min"/>
              <maximalPower value="9572" unit="kW"/>
            </measurement>
          </ambientTemperature>
        </maximalPowerMeasurements>
      </gasTurbine>
<!-- Antrieb einer leistungsstaerksten Maschine
      <gasTurbine id="P_RB211_a">
        <energy_rate_fun_coeff_1 value="14400"/>
        <energy_rate_fun_coeff_2 value="2.25"/>
        <energy_rate_fun_coeff_3 value="-5.25155e-20"/>
        <power_fun_coeff_1 value="376.592"/>
        <power_fun_coeff_2 value="7.86492"/>
        <power_fun_coeff_3 value="-0.000733069"/>
        <power_fun_coeff_4 value="-3.687"/>
        <power_fun_coeff_5 value="-0.0479288"/>
        <power_fun_coeff_6 value="4.45636e-06"/>
        <power_fun_coeff_7 value="0.064625"/>
        <power_fun_coeff_8 value="2.95673e-05"/>
        <power_fun_coeff_9 value="-2.31139e-09"/>
        <specificEnergyConsumptionMeasurements>
          <measurement>
            <compressorPower value="21200" unit="kW"/>
            <fuelConsumption value="62100" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="20000" unit="kW"/>
            <fuelConsumption value="59400" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="16000" unit="kW"/>
            <fuelConsumption value="50400" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="12000" unit="kW"/>
            <fuelConsumption value="41400" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="8000" unit="kW"/>
            <fuelConsumption value="32400" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="4000" unit="kW"/>
            <fuelConsumption value="23400" unit="kW"/>
          </measurement>
        </specificEnergyConsumptionMeasurements>
        <maximalPowerMeasurements>
          <ambientTemperature value="-5" unit="C">
            <measurement>
              <speed value="3640" unit="per_min"/>
              <maximalPower value="19891" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="4160" unit="per_min"/>
              <maximalPower value="21042" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="4680" unit="per_min"/>
              <maximalPower value="21784" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="5200" unit="per_min"/>
              <maximalPower value="22118" unit="kW"/>
            </measurement>
          </ambientTemperature>
          <ambientTemperature value="5" unit="C">
            <measurement>
              <speed value="3640" unit="per_min"/>
              <maximalPower value="18700" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="4160" unit="per_min"/>
              <maximalPower value="19783" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="4680" unit="per_min"/>
              <maximalPower value="20480" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="5200" unit="per_min"/>
              <maximalPower value="20794" unit="kW"/>
            </measurement>
          </ambientTemperature>
          <ambientTemperature value="15" unit="C">
            <measurement>
              <speed value="3640" unit="per_min"/>
              <maximalPower value="17537" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="4160" unit="per_min"/>
              <maximalPower value="18553" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="4680" unit="per_min"/>
              <maximalPower value="19206" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="5200" unit="per_min"/>
              <maximalPower value="19501" unit="kW"/>
            </measurement>
          </ambientTemperature>
          <ambientTemperature value="25" unit="C">
            <measurement>
              <speed value="3640" unit="per_min"/>
              <maximalPower value="16403" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="4160" unit="per_min"/>
              <maximalPower value="17352" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="4680" unit="per_min"/>
              <maximalPower value="17964" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="5200" unit="per_min"/>
              <maximalPower value="18239" unit="kW"/>
            </measurement>
          </ambientTemperature>
        </maximalPowerMeasurements>
      </gasTurbine>
-->
<!-- ending of the drives id renamed to avoid naming conflicts -->
      <gasDrivenMotor id="M_16Z330_a">
        <energy_rate_fun_coeff_1 value="2629"/>
        <energy_rate_fun_coeff_2 value="2.47429"/>
        <energy_rate_fun_coeff_3 value="1.37143e-05"/>
        <power_fun_coeff_1 value="230.515"/>
        <power_fun_coeff_2 value="24.4196"/>
        <power_fun_coeff_3 value="0.00423351"/>
        <specificEnergyConsumptionMeasurements>
          <measurement>
            <compressorPower value="5250" unit="kW"/>
            <fuelConsumption value="15997" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="7000" unit="kW"/>
            <fuelConsumption value="20621" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="8750" unit="kW"/>
            <fuelConsumption value="25329" unit="kW"/>
          </measurement>
        </specificEnergyConsumptionMeasurements>
        <maximalPowerMeasurements>
          <measurement>
            <speed value="165" unit="per_min"/>
            <maximalPower value="4375" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="250" unit="per_min"/>
            <maximalPower value="6600" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="330" unit="per_min"/>
            <maximalPower value="8750" unit="kW"/>
          </measurement>
        </maximalPowerMeasurements>
      </gasDrivenMotor>
<!-- ending of the drives id renamed to avoid naming conflicts -->
      <gasDrivenMotor id="M_16Z330_b">
        <energy_rate_fun_coeff_1 value="2629"/>
        <energy_rate_fun_coeff_2 value="2.47429"/>
        <energy_rate_fun_coeff_3 value="1.37143e-05"/>
        <power_fun_coeff_1 value="230.515"/>
        <power_fun_coeff_2 value="24.4196"/>
        <power_fun_coeff_3 value="0.00423351"/>
        <specificEnergyConsumptionMeasurements>
          <measurement>
            <compressorPower value="5250" unit="kW"/>
            <fuelConsumption value="15997" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="7000" unit="kW"/>
            <fuelConsumption value="20621" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="8750" unit="kW"/>
            <fuelConsumption value="25329" unit="kW"/>
          </measurement>
        </specificEnergyConsumptionMeasurements>
        <maximalPowerMeasurements>
          <measurement>
            <speed value="165" unit="per_min"/>
            <maximalPower value="4375" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="250" unit="per_min"/>
            <maximalPower value="6600" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="330" unit="per_min"/>
            <maximalPower value="8750" unit="kW"/>
          </measurement>
        </maximalPowerMeasurements>
      </gasDrivenMotor>
    </drives>
    <configurations>
      <configuration confId="1" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="1">
          <compressor id="K_KRUH_M1" nominalSpeed="310"/>
        </stage>
      </configuration>
<!-- symmetrisch zu 1 (Armin Fuegenschuh, 07.09.2010)
      <configuration confId="2" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="1">
          <compressor id="K_KRUH_M2" nominalSpeed="310"/>
        </stage>
      </configuration>
-->
      <configuration confId="3" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="1">
          <compressor id="T_KRUH_M3" nominalSpeed="6500"/>
        </stage>
      </configuration>
<!-- not active (Info: Uwe Gotzes, 14. Juni 2010)
      <configuration confId="4" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="1">
          <compressor id="T_KRUH_M4" nominalSpeed="4250"/>
        </stage>
      </configuration>
-->
      <configuration confId="P12" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="2">
          <compressor id="K_KRUH_M1" nominalSpeed="310"/>
          <compressor id="K_KRUH_M2" nominalSpeed="310"/>
        </stage>
      </configuration>
      <configuration confId="P13" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="2">
          <compressor id="K_KRUH_M1" nominalSpeed="310"/>
          <compressor id="T_KRUH_M3" nominalSpeed="6500"/>
        </stage>
      </configuration>
<!-- redandand, because of P12 and 1 and 2 being the same machines
      <configuration confId="P23" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="2">
          <compressor id="K_KRUH_M2" nominalSpeed="310"/>
          <compressor id="T_KRUH_M3" nominalSpeed="6500"/>
        </stage>
      </configuration>
-->
<!-- not active (Info: Uwe Gotzes, 14. Juni 2010)
      <configuration confId="P14" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="2">
          <compressor id="K_KRUH_M1" nominalSpeed="310"/>
          <compressor id="T_KRUH_M4" nominalSpeed="4250"/>
        </stage>
      </configuration>
-->
<!-- not active (Info: Uwe Gotzes, 14. Juni 2010)
      <configuration confId="P24" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="2">
          <compressor id="K_KRUH_M2" nominalSpeed="310"/>
          <compressor id="T_KRUH_M4" nominalSpeed="4250"/>
        </stage>
      </configuration>
-->
      <configuration confId="P123" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="3">
          <compressor id="K_KRUH_M1" nominalSpeed="310"/>
          <compressor id="K_KRUH_M2" nominalSpeed="310"/>
          <compressor id="T_KRUH_M3" nominalSpeed="6500"/>
        </stage>
      </configuration>
<!-- not active (Info: Uwe Gotzes, 14. Juni 2010)
      <configuration confId="P124" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="3">
          <compressor id="K_KRUH_M1" nominalSpeed="310"/>
          <compressor id="K_KRUH_M2" nominalSpeed="310"/>
          <compressor id="T_KRUH_M4" nominalSpeed="4250"/>
        </stage>
      </configuration>
-->
<!-- not active (Info: Uwe Gotzes, 14. Juni 2010)
      <configuration confId="P134" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="3">
          <compressor id="K_KRUH_M1" nominalSpeed="310"/>
          <compressor id="T_KRUH_M3" nominalSpeed="6500"/>
          <compressor id="T_KRUH_M4" nominalSpeed="4250"/>
        </stage>
      </configuration>
-->
<!-- not active (Info: Uwe Gotzes, 14. Juni 2010)
      <configuration confId="P234" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="3">
          <compressor id="K_KRUH_M2" nominalSpeed="310"/>
          <compressor id="T_KRUH_M3" nominalSpeed="6500"/>
          <compressor id="T_KRUH_M4" nominalSpeed="4250"/>
        </stage>
      </configuration>
-->
<!-- not active (Info: Uwe Gotzes, 14. Juni 2010)
      <configuration confId="P1234" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="4">
          <compressor id="K_KRUH_M1" nominalSpeed="310"/>
          <compressor id="K_KRUH_M2" nominalSpeed="310"/>
          <compressor id="T_KRUH_M3" nominalSpeed="6500"/>
          <compressor id="T_KRUH_M4" nominalSpeed="4250"/>
        </stage>
      </configuration>
-->
    </configurations>
  </compressorStation>
  <compressorStation id="eN_WARD_ME_1">
    <compressors>
<!-- ending of the drives id renamed to avoid naming conflicts -->
      <turboCompressor drive="E_WARD_a" id="T_WARD_P">
        <speedMin value="3419" unit="per_min"/>
        <speedMax value="11999" unit="per_min"/>
        <n_isoline_coeff_1 value="5.02329"/>
        <n_isoline_coeff_2 value="-0.00112713"/>
        <n_isoline_coeff_3 value="3.86424e-08"/>
        <n_isoline_coeff_4 value="-4.79098"/>
        <n_isoline_coeff_5 value="0.00263849"/>
        <n_isoline_coeff_6 value="-5.37136e-08"/>
        <n_isoline_coeff_7 value="-1.35378"/>
        <n_isoline_coeff_8 value="-0.000207661"/>
        <n_isoline_coeff_9 value="1.11141e-08"/>
        <eta_ad_isoline_coeff_1 value="1.31043"/>
        <eta_ad_isoline_coeff_2 value="-8.81211e-05"/>
        <eta_ad_isoline_coeff_3 value="-1.1675e-08"/>
        <eta_ad_isoline_coeff_4 value="-0.538182"/>
        <eta_ad_isoline_coeff_5 value="0.000220885"/>
        <eta_ad_isoline_coeff_6 value="-8.30083e-09"/>
        <eta_ad_isoline_coeff_7 value="-0.0933607"/>
        <eta_ad_isoline_coeff_8 value="-8.53102e-06"/>
        <eta_ad_isoline_coeff_9 value="7.44334e-10"/>
        <surgeline_coeff_1 value="1.17177"/>
        <surgeline_coeff_2 value="-1.07506"/>
        <surgeline_coeff_3 value="4.1616"/>
        <chokeline_coeff_1 value="-2.91667"/>
        <chokeline_coeff_2 value="-0.0384949"/>
        <chokeline_coeff_3 value="0.336048"/>
        <efficiencyOfChokeline value="0.49"/>
        <settlelineMeasurements>
          <measurement>
            <speed value="3429" unit="per_min"/>
            <adiabaticHead value="2.9" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.82" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="5714" unit="per_min"/>
            <adiabaticHead value="8.4" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.41" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="8000" unit="per_min"/>
            <adiabaticHead value="16.8" unit="kJ_per_kg"/>
            <volumetricFlowrate value="2.1" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="9142" unit="per_min"/>
            <adiabaticHead value="22" unit="kJ_per_kg"/>
            <volumetricFlowrate value="2.36" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="10285" unit="per_min"/>
            <adiabaticHead value="28" unit="kJ_per_kg"/>
            <volumetricFlowrate value="2.67" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="11428" unit="per_min"/>
            <adiabaticHead value="34.9" unit="kJ_per_kg"/>
            <volumetricFlowrate value="3" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="11999" unit="per_min"/>
            <adiabaticHead value="38.3" unit="kJ_per_kg"/>
            <volumetricFlowrate value="3.1" unit="m_cube_per_s"/>
          </measurement>
        </settlelineMeasurements>
        <characteristicDiagramMeasurements>
          <adiabaticEfficiency value="0.85">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="3.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="8.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.64" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="16.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.43" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="21.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.79" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="27.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.14" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="34.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.57" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="37.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.8" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.87">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="2.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="7.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="14.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="19.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.93" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="24.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.43" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="31.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.71" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="34.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.2" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.8">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.79" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="5.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.07" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="11.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.36" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="15.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="19.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.61" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="24.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.21" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="26.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.7" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.75">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="1.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.86" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="5.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.21" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="10.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.54" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="13.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.18" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="17.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.82" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="22" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.46" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="24.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.9" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.64">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="1.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.93" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="4.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.29" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="8.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.64" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="11.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.29" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="14.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.96" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="18.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.64" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="20.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="7.1" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.49">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="1.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="3.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.38" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="6.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.76" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="8.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.48" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="10.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.15" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="13.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.86" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="15.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="7.2" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
        </characteristicDiagramMeasurements>
      </turboCompressor>
<!-- ending of the drives id renamed to avoid naming conflicts -->
      <turboCompressor drive="E_WARD_b" id="T_WARD_S">
        <speedMin value="3419" unit="per_min"/>
        <speedMax value="11999" unit="per_min"/>
        <n_isoline_coeff_1 value="-1.69617"/>
        <n_isoline_coeff_2 value="0.000167784"/>
        <n_isoline_coeff_3 value="-1.39462e-07"/>
        <n_isoline_coeff_4 value="3.2856"/>
        <n_isoline_coeff_5 value="0.00769738"/>
        <n_isoline_coeff_6 value="-1.15745e-07"/>
        <n_isoline_coeff_7 value="-25.8062"/>
        <n_isoline_coeff_8 value="0.000756482"/>
        <n_isoline_coeff_9 value="-1.29816e-09"/>
        <eta_ad_isoline_coeff_1 value="1.04892"/>
        <eta_ad_isoline_coeff_2 value="-1.46922e-05"/>
        <eta_ad_isoline_coeff_3 value="-1.70474e-08"/>
        <eta_ad_isoline_coeff_4 value="-0.741477"/>
        <eta_ad_isoline_coeff_5 value="0.000365699"/>
        <eta_ad_isoline_coeff_6 value="-1.27506e-08"/>
        <eta_ad_isoline_coeff_7 value="-0.504146"/>
        <eta_ad_isoline_coeff_8 value="-5.66371e-06"/>
        <eta_ad_isoline_coeff_9 value="1.70961e-09"/>
        <surgeline_coeff_1 value="-4.01383"/>
        <surgeline_coeff_2 value="13.1671"/>
        <surgeline_coeff_3 value="18.1241"/>
        <chokeline_coeff_1 value="-9.72009"/>
        <chokeline_coeff_2 value="6.91172"/>
        <chokeline_coeff_3 value="1.45443"/>
        <efficiencyOfChokeline value="0.59"/>
        <settlelineMeasurements>
          <measurement>
            <speed value="3429" unit="per_min"/>
            <adiabaticHead value="6.2" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.46" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="5714" unit="per_min"/>
            <adiabaticHead value="16.2" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.76" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="8000" unit="per_min"/>
            <adiabaticHead value="32.6" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.12" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="9142" unit="per_min"/>
            <adiabaticHead value="42.1" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.28" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="10285" unit="per_min"/>
            <adiabaticHead value="54.1" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.44" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="11428" unit="per_min"/>
            <adiabaticHead value="67.2" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.64" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="11999" unit="per_min"/>
            <adiabaticHead value="74.1" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.76" unit="m_cube_per_s"/>
          </measurement>
        </settlelineMeasurements>
        <characteristicDiagramMeasurements>
          <adiabaticEfficiency value="0.85">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="5.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.47" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="16.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.83" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="32.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.23" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="42.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.43" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="53.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.67" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="66.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.91" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="72" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.24" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.86">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="5.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.57" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="15.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="30.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.46" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="40" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.71" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="51.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="63.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.31" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="70" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.52" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.8">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="4.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.89" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="11.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.54" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="23.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.17" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="30.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.49" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="39.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.86" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="48.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.23" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="52.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.52" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.75">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="3.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.91" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="10.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.54" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="21.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.2" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="28.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.57" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="36.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.94" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="44.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.4" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="47.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.64" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.64">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.94" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="9.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.6" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="18.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.26" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="25" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.61" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="31.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="39.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.46" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="40" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.68" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.59">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.95" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="7.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.65" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="15.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.34" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="20.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.7" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="26.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.09" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="32.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.48" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="36" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.69" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
        </characteristicDiagramMeasurements>
      </turboCompressor>
    </compressors>
    <drives>
<!-- ending of the drives id renamed to avoid naming conflicts -->
      <electricMotor id="E_WARD_a">
<!-- Using linear instead of quadratic LS-approximation because of negative slope -->
        <energy_rate_fun_coeff_1 value="0.01"/>
        <energy_rate_fun_coeff_2 value="4.92718e-22"/>
        <energy_rate_fun_coeff_3 value="0"/>
        <power_fun_coeff_1 value="-1823.18"/>
        <power_fun_coeff_2 value="1.10551"/>
        <power_fun_coeff_3 value="-3.4413e-05"/>
        <specificEnergyConsumptionMeasurements>
          <measurement>
            <compressorPower value="1311" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="2622" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="5520" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="6831" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
        </specificEnergyConsumptionMeasurements>
        <maximalPowerMeasurements>
          <measurement>
            <speed value="3419" unit="per_min"/>
            <maximalPower value="1311" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="4323" unit="per_min"/>
            <maximalPower value="2622" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="9161" unit="per_min"/>
            <maximalPower value="5520" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="10193" unit="per_min"/>
            <maximalPower value="5520" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11032" unit="per_min"/>
            <maximalPower value="6176" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11548" unit="per_min"/>
            <maximalPower value="6176" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11677" unit="per_min"/>
            <maximalPower value="6831" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11999" unit="per_min"/>
            <maximalPower value="6417" unit="kW"/>
          </measurement>
        </maximalPowerMeasurements>
      </electricMotor>
<!-- ending of the drives id renamed to avoid naming conflicts -->
      <electricMotor id="E_WARD_b">
<!-- Using linear instead of quadratic LS-approximation because of negative slope -->
        <energy_rate_fun_coeff_1 value="0.01"/>
        <energy_rate_fun_coeff_2 value="4.92718e-22"/>
        <energy_rate_fun_coeff_3 value="0"/>
        <power_fun_coeff_1 value="-1823.18"/>
        <power_fun_coeff_2 value="1.10551"/>
        <power_fun_coeff_3 value="-3.4413e-05"/>
        <specificEnergyConsumptionMeasurements>
          <measurement>
            <compressorPower value="1311" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="2622" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="5520" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="6831" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
        </specificEnergyConsumptionMeasurements>
        <maximalPowerMeasurements>
          <measurement>
            <speed value="3419" unit="per_min"/>
            <maximalPower value="1311" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="4323" unit="per_min"/>
            <maximalPower value="2622" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="9161" unit="per_min"/>
            <maximalPower value="5520" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="10193" unit="per_min"/>
            <maximalPower value="5520" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11032" unit="per_min"/>
            <maximalPower value="6176" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11548" unit="per_min"/>
            <maximalPower value="6176" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11677" unit="per_min"/>
            <maximalPower value="6831" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11999" unit="per_min"/>
            <maximalPower value="6417" unit="kW"/>
          </measurement>
        </maximalPowerMeasurements>
      </electricMotor>
    </drives>
    <configurations>
      <configuration confId="p" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="1">
          <compressor id="T_WARD_P" nominalSpeed="11000"/>
        </stage>
      </configuration>
      <configuration confId="s" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="1">
          <compressor id="T_WARD_S" nominalSpeed="11000"/>
        </stage>
      </configuration>
    </configurations>
  </compressorStation>
  <compressorStation id="eN_WARD_ME_2">
    <compressors>
<!-- ending of the drives id renamed to avoid naming conflicts -->
      <turboCompressor drive="E_WARD_a" id="T_WARD_P">
        <speedMin value="3419" unit="per_min"/>
        <speedMax value="11999" unit="per_min"/>
        <n_isoline_coeff_1 value="5.02329"/>
        <n_isoline_coeff_2 value="-0.00112713"/>
        <n_isoline_coeff_3 value="3.86424e-08"/>
        <n_isoline_coeff_4 value="-4.79098"/>
        <n_isoline_coeff_5 value="0.00263849"/>
        <n_isoline_coeff_6 value="-5.37136e-08"/>
        <n_isoline_coeff_7 value="-1.35378"/>
        <n_isoline_coeff_8 value="-0.000207661"/>
        <n_isoline_coeff_9 value="1.11141e-08"/>
        <eta_ad_isoline_coeff_1 value="1.31043"/>
        <eta_ad_isoline_coeff_2 value="-8.81211e-05"/>
        <eta_ad_isoline_coeff_3 value="-1.1675e-08"/>
        <eta_ad_isoline_coeff_4 value="-0.538182"/>
        <eta_ad_isoline_coeff_5 value="0.000220885"/>
        <eta_ad_isoline_coeff_6 value="-8.30083e-09"/>
        <eta_ad_isoline_coeff_7 value="-0.0933607"/>
        <eta_ad_isoline_coeff_8 value="-8.53102e-06"/>
        <eta_ad_isoline_coeff_9 value="7.44334e-10"/>
        <surgeline_coeff_1 value="1.17177"/>
        <surgeline_coeff_2 value="-1.07506"/>
        <surgeline_coeff_3 value="4.1616"/>
        <chokeline_coeff_1 value="-2.91667"/>
        <chokeline_coeff_2 value="-0.0384949"/>
        <chokeline_coeff_3 value="0.336048"/>
        <efficiencyOfChokeline value="0.49"/>
        <settlelineMeasurements>
          <measurement>
            <speed value="3429" unit="per_min"/>
            <adiabaticHead value="2.9" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.82" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="5714" unit="per_min"/>
            <adiabaticHead value="8.4" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.41" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="8000" unit="per_min"/>
            <adiabaticHead value="16.8" unit="kJ_per_kg"/>
            <volumetricFlowrate value="2.1" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="9142" unit="per_min"/>
            <adiabaticHead value="22" unit="kJ_per_kg"/>
            <volumetricFlowrate value="2.36" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="10285" unit="per_min"/>
            <adiabaticHead value="28" unit="kJ_per_kg"/>
            <volumetricFlowrate value="2.67" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="11428" unit="per_min"/>
            <adiabaticHead value="34.9" unit="kJ_per_kg"/>
            <volumetricFlowrate value="3" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="11999" unit="per_min"/>
            <adiabaticHead value="38.3" unit="kJ_per_kg"/>
            <volumetricFlowrate value="3.1" unit="m_cube_per_s"/>
          </measurement>
        </settlelineMeasurements>
        <characteristicDiagramMeasurements>
          <adiabaticEfficiency value="0.85">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="3.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="8.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.64" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="16.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.43" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="21.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.79" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="27.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.14" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="34.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.57" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="37.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.8" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.87">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="2.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="7.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="14.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="19.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.93" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="24.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.43" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="31.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.71" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="34.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.2" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.8">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.79" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="5.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.07" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="11.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.36" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="15.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="19.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.61" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="24.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.21" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="26.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.7" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.75">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="1.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.86" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="5.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.21" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="10.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.54" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="13.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.18" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="17.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.82" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="22" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.46" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="24.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.9" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.64">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="1.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.93" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="4.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.29" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="8.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.64" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="11.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.29" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="14.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.96" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="18.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.64" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="20.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="7.1" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.49">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="1.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="3.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.38" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="6.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.76" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="8.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.48" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="10.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.15" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="13.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.86" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="15.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="7.2" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
        </characteristicDiagramMeasurements>
      </turboCompressor>
<!-- ending of the drives id renamed to avoid naming conflicts -->
      <turboCompressor drive="E_WARD_b" id="T_WARD_S">
        <speedMin value="3419" unit="per_min"/>
        <speedMax value="11999" unit="per_min"/>
        <n_isoline_coeff_1 value="-1.69617"/>
        <n_isoline_coeff_2 value="0.000167784"/>
        <n_isoline_coeff_3 value="-1.39462e-07"/>
        <n_isoline_coeff_4 value="3.2856"/>
        <n_isoline_coeff_5 value="0.00769738"/>
        <n_isoline_coeff_6 value="-1.15745e-07"/>
        <n_isoline_coeff_7 value="-25.8062"/>
        <n_isoline_coeff_8 value="0.000756482"/>
        <n_isoline_coeff_9 value="-1.29816e-09"/>
        <eta_ad_isoline_coeff_1 value="1.04892"/>
        <eta_ad_isoline_coeff_2 value="-1.46922e-05"/>
        <eta_ad_isoline_coeff_3 value="-1.70474e-08"/>
        <eta_ad_isoline_coeff_4 value="-0.741477"/>
        <eta_ad_isoline_coeff_5 value="0.000365699"/>
        <eta_ad_isoline_coeff_6 value="-1.27506e-08"/>
        <eta_ad_isoline_coeff_7 value="-0.504146"/>
        <eta_ad_isoline_coeff_8 value="-5.66371e-06"/>
        <eta_ad_isoline_coeff_9 value="1.70961e-09"/>
        <surgeline_coeff_1 value="-4.01383"/>
        <surgeline_coeff_2 value="13.1671"/>
        <surgeline_coeff_3 value="18.1241"/>
        <chokeline_coeff_1 value="-9.72009"/>
        <chokeline_coeff_2 value="6.91172"/>
        <chokeline_coeff_3 value="1.45443"/>
        <efficiencyOfChokeline value="0.59"/>
        <settlelineMeasurements>
          <measurement>
            <speed value="3429" unit="per_min"/>
            <adiabaticHead value="6.2" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.46" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="5714" unit="per_min"/>
            <adiabaticHead value="16.2" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.76" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="8000" unit="per_min"/>
            <adiabaticHead value="32.6" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.12" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="9142" unit="per_min"/>
            <adiabaticHead value="42.1" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.28" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="10285" unit="per_min"/>
            <adiabaticHead value="54.1" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.44" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="11428" unit="per_min"/>
            <adiabaticHead value="67.2" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.64" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="11999" unit="per_min"/>
            <adiabaticHead value="74.1" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.76" unit="m_cube_per_s"/>
          </measurement>
        </settlelineMeasurements>
        <characteristicDiagramMeasurements>
          <adiabaticEfficiency value="0.85">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="5.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.47" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="16.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.83" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="32.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.23" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="42.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.43" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="53.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.67" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="66.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.91" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="72" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.24" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.86">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="5.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.57" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="15.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="30.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.46" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="40" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.71" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="51.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="63.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.31" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="70" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.52" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.8">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="4.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.89" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="11.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.54" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="23.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.17" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="30.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.49" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="39.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.86" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="48.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.23" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="52.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.52" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.75">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="3.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.91" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="10.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.54" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="21.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.2" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="28.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.57" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="36.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.94" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="44.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.4" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="47.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.64" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.64">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.94" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="9.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.6" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="18.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.26" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="25" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.61" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="31.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="39.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.46" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="40" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.68" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.59">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.95" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="7.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.65" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="15.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.34" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="20.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.7" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="26.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.09" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="32.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.48" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="36" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.69" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
        </characteristicDiagramMeasurements>
      </turboCompressor>
    </compressors>
    <drives>
<!-- ending of the drives id renamed to avoid naming conflicts -->
      <electricMotor id="E_WARD_a">
<!-- Using linear instead of quadratic LS-approximation because of negative slope -->
        <energy_rate_fun_coeff_1 value="0.01"/>
        <energy_rate_fun_coeff_2 value="4.92718e-22"/>
        <energy_rate_fun_coeff_3 value="0"/>
        <power_fun_coeff_1 value="-1823.18"/>
        <power_fun_coeff_2 value="1.10551"/>
        <power_fun_coeff_3 value="-3.4413e-05"/>
        <specificEnergyConsumptionMeasurements>
          <measurement>
            <compressorPower value="1311" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="2622" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="5520" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="6831" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
        </specificEnergyConsumptionMeasurements>
        <maximalPowerMeasurements>
          <measurement>
            <speed value="3419" unit="per_min"/>
            <maximalPower value="1311" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="4323" unit="per_min"/>
            <maximalPower value="2622" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="9161" unit="per_min"/>
            <maximalPower value="5520" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="10193" unit="per_min"/>
            <maximalPower value="5520" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11032" unit="per_min"/>
            <maximalPower value="6176" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11548" unit="per_min"/>
            <maximalPower value="6176" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11677" unit="per_min"/>
            <maximalPower value="6831" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11999" unit="per_min"/>
            <maximalPower value="6417" unit="kW"/>
          </measurement>
        </maximalPowerMeasurements>
      </electricMotor>
<!-- ending of the drives id renamed to avoid naming conflicts -->
      <electricMotor id="E_WARD_b">
<!-- Using linear instead of quadratic LS-approximation because of negative slope -->
        <energy_rate_fun_coeff_1 value="0.01"/>
        <energy_rate_fun_coeff_2 value="4.92718e-22"/>
        <energy_rate_fun_coeff_3 value="0"/>
        <power_fun_coeff_1 value="-1823.18"/>
        <power_fun_coeff_2 value="1.10551"/>
        <power_fun_coeff_3 value="-3.4413e-05"/>
        <specificEnergyConsumptionMeasurements>
          <measurement>
            <compressorPower value="1311" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="2622" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="5520" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="6831" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
        </specificEnergyConsumptionMeasurements>
        <maximalPowerMeasurements>
          <measurement>
            <speed value="3419" unit="per_min"/>
            <maximalPower value="1311" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="4323" unit="per_min"/>
            <maximalPower value="2622" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="9161" unit="per_min"/>
            <maximalPower value="5520" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="10193" unit="per_min"/>
            <maximalPower value="5520" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11032" unit="per_min"/>
            <maximalPower value="6176" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11548" unit="per_min"/>
            <maximalPower value="6176" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11677" unit="per_min"/>
            <maximalPower value="6831" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11999" unit="per_min"/>
            <maximalPower value="6417" unit="kW"/>
          </measurement>
        </maximalPowerMeasurements>
      </electricMotor>
    </drives>
    <configurations>
      <configuration confId="p" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="1">
          <compressor id="T_WARD_P" nominalSpeed="11000"/>
        </stage>
      </configuration>
      <configuration confId="s" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="1">
          <compressor id="T_WARD_S" nominalSpeed="11000"/>
        </stage>
      </configuration>
    </configurations>
  </compressorStation>
  <compressorStation id="eN_WARD_ME_3">
    <compressors>
<!-- ending of the drives id renamed to avoid naming conflicts -->
      <turboCompressor drive="E_WARD_a" id="T_WARD_P">
        <speedMin value="3419" unit="per_min"/>
        <speedMax value="11999" unit="per_min"/>
        <n_isoline_coeff_1 value="5.02329"/>
        <n_isoline_coeff_2 value="-0.00112713"/>
        <n_isoline_coeff_3 value="3.86424e-08"/>
        <n_isoline_coeff_4 value="-4.79098"/>
        <n_isoline_coeff_5 value="0.00263849"/>
        <n_isoline_coeff_6 value="-5.37136e-08"/>
        <n_isoline_coeff_7 value="-1.35378"/>
        <n_isoline_coeff_8 value="-0.000207661"/>
        <n_isoline_coeff_9 value="1.11141e-08"/>
        <eta_ad_isoline_coeff_1 value="1.31043"/>
        <eta_ad_isoline_coeff_2 value="-8.81211e-05"/>
        <eta_ad_isoline_coeff_3 value="-1.1675e-08"/>
        <eta_ad_isoline_coeff_4 value="-0.538182"/>
        <eta_ad_isoline_coeff_5 value="0.000220885"/>
        <eta_ad_isoline_coeff_6 value="-8.30083e-09"/>
        <eta_ad_isoline_coeff_7 value="-0.0933607"/>
        <eta_ad_isoline_coeff_8 value="-8.53102e-06"/>
        <eta_ad_isoline_coeff_9 value="7.44334e-10"/>
        <surgeline_coeff_1 value="1.17177"/>
        <surgeline_coeff_2 value="-1.07506"/>
        <surgeline_coeff_3 value="4.1616"/>
        <chokeline_coeff_1 value="-2.91667"/>
        <chokeline_coeff_2 value="-0.0384949"/>
        <chokeline_coeff_3 value="0.336048"/>
        <efficiencyOfChokeline value="0.49"/>
        <settlelineMeasurements>
          <measurement>
            <speed value="3429" unit="per_min"/>
            <adiabaticHead value="2.9" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.82" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="5714" unit="per_min"/>
            <adiabaticHead value="8.4" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.41" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="8000" unit="per_min"/>
            <adiabaticHead value="16.8" unit="kJ_per_kg"/>
            <volumetricFlowrate value="2.1" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="9142" unit="per_min"/>
            <adiabaticHead value="22" unit="kJ_per_kg"/>
            <volumetricFlowrate value="2.36" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="10285" unit="per_min"/>
            <adiabaticHead value="28" unit="kJ_per_kg"/>
            <volumetricFlowrate value="2.67" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="11428" unit="per_min"/>
            <adiabaticHead value="34.9" unit="kJ_per_kg"/>
            <volumetricFlowrate value="3" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="11999" unit="per_min"/>
            <adiabaticHead value="38.3" unit="kJ_per_kg"/>
            <volumetricFlowrate value="3.1" unit="m_cube_per_s"/>
          </measurement>
        </settlelineMeasurements>
        <characteristicDiagramMeasurements>
          <adiabaticEfficiency value="0.85">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="3.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="8.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.64" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="16.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.43" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="21.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.79" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="27.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.14" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="34.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.57" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="37.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.8" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.87">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="2.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="7.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="14.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="19.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.93" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="24.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.43" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="31.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.71" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="34.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.2" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.8">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.79" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="5.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.07" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="11.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.36" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="15.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="19.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.61" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="24.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.21" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="26.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.7" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.75">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="1.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.86" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="5.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.21" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="10.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.54" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="13.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.18" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="17.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.82" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="22" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.46" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="24.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.9" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.64">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="1.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.93" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="4.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.29" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="8.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.64" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="11.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.29" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="14.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.96" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="18.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.64" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="20.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="7.1" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.49">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="1.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="3.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.38" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="6.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.76" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="8.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.48" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="10.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.15" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="13.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.86" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="15.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="7.2" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
        </characteristicDiagramMeasurements>
      </turboCompressor>
<!-- ending of the drives id renamed to avoid naming conflicts -->
      <turboCompressor drive="E_WARD_b" id="T_WARD_S">
        <speedMin value="3419" unit="per_min"/>
        <speedMax value="11999" unit="per_min"/>
        <n_isoline_coeff_1 value="-1.69617"/>
        <n_isoline_coeff_2 value="0.000167784"/>
        <n_isoline_coeff_3 value="-1.39462e-07"/>
        <n_isoline_coeff_4 value="3.2856"/>
        <n_isoline_coeff_5 value="0.00769738"/>
        <n_isoline_coeff_6 value="-1.15745e-07"/>
        <n_isoline_coeff_7 value="-25.8062"/>
        <n_isoline_coeff_8 value="0.000756482"/>
        <n_isoline_coeff_9 value="-1.29816e-09"/>
        <eta_ad_isoline_coeff_1 value="1.04892"/>
        <eta_ad_isoline_coeff_2 value="-1.46922e-05"/>
        <eta_ad_isoline_coeff_3 value="-1.70474e-08"/>
        <eta_ad_isoline_coeff_4 value="-0.741477"/>
        <eta_ad_isoline_coeff_5 value="0.000365699"/>
        <eta_ad_isoline_coeff_6 value="-1.27506e-08"/>
        <eta_ad_isoline_coeff_7 value="-0.504146"/>
        <eta_ad_isoline_coeff_8 value="-5.66371e-06"/>
        <eta_ad_isoline_coeff_9 value="1.70961e-09"/>
        <surgeline_coeff_1 value="-4.01383"/>
        <surgeline_coeff_2 value="13.1671"/>
        <surgeline_coeff_3 value="18.1241"/>
        <chokeline_coeff_1 value="-9.72009"/>
        <chokeline_coeff_2 value="6.91172"/>
        <chokeline_coeff_3 value="1.45443"/>
        <efficiencyOfChokeline value="0.59"/>
        <settlelineMeasurements>
          <measurement>
            <speed value="3429" unit="per_min"/>
            <adiabaticHead value="6.2" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.46" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="5714" unit="per_min"/>
            <adiabaticHead value="16.2" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.76" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="8000" unit="per_min"/>
            <adiabaticHead value="32.6" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.12" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="9142" unit="per_min"/>
            <adiabaticHead value="42.1" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.28" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="10285" unit="per_min"/>
            <adiabaticHead value="54.1" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.44" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="11428" unit="per_min"/>
            <adiabaticHead value="67.2" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.64" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="11999" unit="per_min"/>
            <adiabaticHead value="74.1" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.76" unit="m_cube_per_s"/>
          </measurement>
        </settlelineMeasurements>
        <characteristicDiagramMeasurements>
          <adiabaticEfficiency value="0.85">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="5.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.47" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="16.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.83" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="32.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.23" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="42.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.43" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="53.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.67" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="66.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.91" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="72" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.24" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.86">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="5.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.57" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="15.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="30.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.46" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="40" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.71" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="51.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="63.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.31" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="70" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.52" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.8">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="4.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.89" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="11.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.54" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="23.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.17" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="30.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.49" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="39.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.86" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="48.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.23" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="52.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.52" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.75">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="3.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.91" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="10.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.54" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="21.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.2" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="28.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.57" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="36.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.94" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="44.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.4" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="47.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.64" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.64">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.94" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="9.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.6" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="18.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.26" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="25" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.61" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="31.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="39.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.46" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="40" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.68" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.59">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.95" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="7.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.65" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="15.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.34" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="20.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.7" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="26.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.09" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="32.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.48" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="36" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.69" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
        </characteristicDiagramMeasurements>
      </turboCompressor>
    </compressors>
    <drives>
<!-- ending of the drives id renamed to avoid naming conflicts -->
      <electricMotor id="E_WARD_a">
<!-- Using linear instead of quadratic LS-approximation because of negative slope -->
        <energy_rate_fun_coeff_1 value="0.01"/>
        <energy_rate_fun_coeff_2 value="4.92718e-22"/>
        <energy_rate_fun_coeff_3 value="0"/>
        <power_fun_coeff_1 value="-1823.18"/>
        <power_fun_coeff_2 value="1.10551"/>
        <power_fun_coeff_3 value="-3.4413e-05"/>
        <specificEnergyConsumptionMeasurements>
          <measurement>
            <compressorPower value="1311" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="2622" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="5520" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="6831" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
        </specificEnergyConsumptionMeasurements>
        <maximalPowerMeasurements>
          <measurement>
            <speed value="3419" unit="per_min"/>
            <maximalPower value="1311" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="4323" unit="per_min"/>
            <maximalPower value="2622" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="9161" unit="per_min"/>
            <maximalPower value="5520" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="10193" unit="per_min"/>
            <maximalPower value="5520" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11032" unit="per_min"/>
            <maximalPower value="6176" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11548" unit="per_min"/>
            <maximalPower value="6176" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11677" unit="per_min"/>
            <maximalPower value="6831" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11999" unit="per_min"/>
            <maximalPower value="6417" unit="kW"/>
          </measurement>
        </maximalPowerMeasurements>
      </electricMotor>
<!-- ending of the drives id renamed to avoid naming conflicts -->
      <electricMotor id="E_WARD_b">
<!-- Using linear instead of quadratic LS-approximation because of negative slope -->
        <energy_rate_fun_coeff_1 value="0.01"/>
        <energy_rate_fun_coeff_2 value="4.92718e-22"/>
        <energy_rate_fun_coeff_3 value="0"/>
        <power_fun_coeff_1 value="-1823.18"/>
        <power_fun_coeff_2 value="1.10551"/>
        <power_fun_coeff_3 value="-3.4413e-05"/>
        <specificEnergyConsumptionMeasurements>
          <measurement>
            <compressorPower value="1311" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="2622" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="5520" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="6831" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
        </specificEnergyConsumptionMeasurements>
        <maximalPowerMeasurements>
          <measurement>
            <speed value="3419" unit="per_min"/>
            <maximalPower value="1311" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="4323" unit="per_min"/>
            <maximalPower value="2622" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="9161" unit="per_min"/>
            <maximalPower value="5520" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="10193" unit="per_min"/>
            <maximalPower value="5520" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11032" unit="per_min"/>
            <maximalPower value="6176" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11548" unit="per_min"/>
            <maximalPower value="6176" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11677" unit="per_min"/>
            <maximalPower value="6831" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11999" unit="per_min"/>
            <maximalPower value="6417" unit="kW"/>
          </measurement>
        </maximalPowerMeasurements>
      </electricMotor>
    </drives>
    <configurations>
      <configuration confId="p" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="1">
          <compressor id="T_WARD_P" nominalSpeed="11000"/>
        </stage>
      </configuration>
      <configuration confId="s" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="1">
          <compressor id="T_WARD_S" nominalSpeed="11000"/>
        </stage>
      </configuration>
    </configurations>
  </compressorStation>
  <compressorStation id="eN_WARD_ME_4">
    <compressors>
      <turboCompressor drive="E_WARD" id="T_WARD_P">
        <speedMin value="3419" unit="per_min"/>
        <speedMax value="11999" unit="per_min"/>
        <n_isoline_coeff_1 value="5.02329"/>
        <n_isoline_coeff_2 value="-0.00112713"/>
        <n_isoline_coeff_3 value="3.86424e-08"/>
        <n_isoline_coeff_4 value="-4.79098"/>
        <n_isoline_coeff_5 value="0.00263849"/>
        <n_isoline_coeff_6 value="-5.37136e-08"/>
        <n_isoline_coeff_7 value="-1.35378"/>
        <n_isoline_coeff_8 value="-0.000207661"/>
        <n_isoline_coeff_9 value="1.11141e-08"/>
        <eta_ad_isoline_coeff_1 value="1.31043"/>
        <eta_ad_isoline_coeff_2 value="-8.81211e-05"/>
        <eta_ad_isoline_coeff_3 value="-1.1675e-08"/>
        <eta_ad_isoline_coeff_4 value="-0.538182"/>
        <eta_ad_isoline_coeff_5 value="0.000220885"/>
        <eta_ad_isoline_coeff_6 value="-8.30083e-09"/>
        <eta_ad_isoline_coeff_7 value="-0.0933607"/>
        <eta_ad_isoline_coeff_8 value="-8.53102e-06"/>
        <eta_ad_isoline_coeff_9 value="7.44334e-10"/>
        <surgeline_coeff_1 value="1.17177"/>
        <surgeline_coeff_2 value="-1.07506"/>
        <surgeline_coeff_3 value="4.1616"/>
        <chokeline_coeff_1 value="-2.91667"/>
        <chokeline_coeff_2 value="-0.0384949"/>
        <chokeline_coeff_3 value="0.336048"/>
        <efficiencyOfChokeline value="0.49"/>
        <settlelineMeasurements>
          <measurement>
            <speed value="3429" unit="per_min"/>
            <adiabaticHead value="2.9" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.82" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="5714" unit="per_min"/>
            <adiabaticHead value="8.4" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.41" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="8000" unit="per_min"/>
            <adiabaticHead value="16.8" unit="kJ_per_kg"/>
            <volumetricFlowrate value="2.1" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="9142" unit="per_min"/>
            <adiabaticHead value="22" unit="kJ_per_kg"/>
            <volumetricFlowrate value="2.36" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="10285" unit="per_min"/>
            <adiabaticHead value="28" unit="kJ_per_kg"/>
            <volumetricFlowrate value="2.67" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="11428" unit="per_min"/>
            <adiabaticHead value="34.9" unit="kJ_per_kg"/>
            <volumetricFlowrate value="3" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="11999" unit="per_min"/>
            <adiabaticHead value="38.3" unit="kJ_per_kg"/>
            <volumetricFlowrate value="3.1" unit="m_cube_per_s"/>
          </measurement>
        </settlelineMeasurements>
        <characteristicDiagramMeasurements>
          <adiabaticEfficiency value="0.85">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="3.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="8.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.64" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="16.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.43" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="21.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.79" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="27.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.14" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="34.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.57" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="37.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.8" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.87">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="2.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="7.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="14.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="19.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.93" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="24.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.43" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="31.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.71" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="34.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.2" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.8">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.79" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="5.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.07" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="11.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.36" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="15.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="19.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.61" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="24.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.21" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="26.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.7" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.75">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="1.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.86" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="5.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.21" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="10.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.54" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="13.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.18" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="17.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.82" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="22" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.46" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="24.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.9" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.64">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="1.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.93" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="4.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.29" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="8.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.64" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="11.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.29" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="14.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.96" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="18.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.64" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="20.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="7.1" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.49">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="1.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="3.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.38" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="6.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="4.76" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="8.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="5.48" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="10.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.15" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="13.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="6.86" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="15.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="7.2" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
        </characteristicDiagramMeasurements>
      </turboCompressor>
      <turboCompressor drive="E_WARD" id="T_WARD_S">
        <speedMin value="3419" unit="per_min"/>
        <speedMax value="11999" unit="per_min"/>
        <n_isoline_coeff_1 value="-1.69617"/>
        <n_isoline_coeff_2 value="0.000167784"/>
        <n_isoline_coeff_3 value="-1.39462e-07"/>
        <n_isoline_coeff_4 value="3.2856"/>
        <n_isoline_coeff_5 value="0.00769738"/>
        <n_isoline_coeff_6 value="-1.15745e-07"/>
        <n_isoline_coeff_7 value="-25.8062"/>
        <n_isoline_coeff_8 value="0.000756482"/>
        <n_isoline_coeff_9 value="-1.29816e-09"/>
        <eta_ad_isoline_coeff_1 value="1.04892"/>
        <eta_ad_isoline_coeff_2 value="-1.46922e-05"/>
        <eta_ad_isoline_coeff_3 value="-1.70474e-08"/>
        <eta_ad_isoline_coeff_4 value="-0.741477"/>
        <eta_ad_isoline_coeff_5 value="0.000365699"/>
        <eta_ad_isoline_coeff_6 value="-1.27506e-08"/>
        <eta_ad_isoline_coeff_7 value="-0.504146"/>
        <eta_ad_isoline_coeff_8 value="-5.66371e-06"/>
        <eta_ad_isoline_coeff_9 value="1.70961e-09"/>
        <surgeline_coeff_1 value="-4.01383"/>
        <surgeline_coeff_2 value="13.1671"/>
        <surgeline_coeff_3 value="18.1241"/>
        <chokeline_coeff_1 value="-9.72009"/>
        <chokeline_coeff_2 value="6.91172"/>
        <chokeline_coeff_3 value="1.45443"/>
        <efficiencyOfChokeline value="0.59"/>
        <settlelineMeasurements>
          <measurement>
            <speed value="3429" unit="per_min"/>
            <adiabaticHead value="6.2" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.46" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="5714" unit="per_min"/>
            <adiabaticHead value="16.2" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.76" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="8000" unit="per_min"/>
            <adiabaticHead value="32.6" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.12" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="9142" unit="per_min"/>
            <adiabaticHead value="42.1" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.28" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="10285" unit="per_min"/>
            <adiabaticHead value="54.1" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.44" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="11428" unit="per_min"/>
            <adiabaticHead value="67.2" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.64" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="11999" unit="per_min"/>
            <adiabaticHead value="74.1" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.76" unit="m_cube_per_s"/>
          </measurement>
        </settlelineMeasurements>
        <characteristicDiagramMeasurements>
          <adiabaticEfficiency value="0.85">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="5.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.47" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="16.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.83" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="32.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.23" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="42.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.43" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="53.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.67" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="66.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.91" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="72" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.24" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.86">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="5.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.57" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="15.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="30.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.46" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="40" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.71" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="51.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="63.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.31" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="70" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.52" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.8">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="4.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.89" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="11.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.54" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="23.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.17" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="30.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.49" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="39.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.86" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="48.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.23" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="52.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.52" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.75">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="3.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.91" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="10.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.54" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="21.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.2" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="28.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.57" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="36.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.94" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="44.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.4" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="47.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.64" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.64">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.94" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="9.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.6" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="18.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.26" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="25" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.61" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="31.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="39.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.46" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="40" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.68" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.59">
            <measurement>
              <speed value="3429" unit="per_min"/>
              <adiabaticHead value="3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.95" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="5714" unit="per_min"/>
              <adiabaticHead value="7.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.65" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8000" unit="per_min"/>
              <adiabaticHead value="15.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.34" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9142" unit="per_min"/>
              <adiabaticHead value="20.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="2.7" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10285" unit="per_min"/>
              <adiabaticHead value="26.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.09" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11428" unit="per_min"/>
              <adiabaticHead value="32.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.48" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11999" unit="per_min"/>
              <adiabaticHead value="36" unit="kJ_per_kg"/>
              <volumetricFlowrate value="3.69" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
        </characteristicDiagramMeasurements>
      </turboCompressor>
    </compressors>
    <drives>
      <electricMotor id="E_WARD">
        <energy_rate_fun_coeff_1 value="0.01"/>
        <energy_rate_fun_coeff_2 value="3.05236e-21"/>
        <energy_rate_fun_coeff_3 value="-3.14375e-25"/>
        <power_fun_coeff_1 value="-1823.18"/>
        <power_fun_coeff_2 value="1.10551"/>
        <power_fun_coeff_3 value="-3.4413e-05"/>
        <specificEnergyConsumptionMeasurements>
          <measurement>
            <compressorPower value="1311" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="2622" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="5520" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="6831" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
        </specificEnergyConsumptionMeasurements>
        <maximalPowerMeasurements>
          <measurement>
            <speed value="3419" unit="per_min"/>
            <maximalPower value="1311" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="4323" unit="per_min"/>
            <maximalPower value="2622" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="9161" unit="per_min"/>
            <maximalPower value="5520" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="10193" unit="per_min"/>
            <maximalPower value="5520" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11032" unit="per_min"/>
            <maximalPower value="6176" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11548" unit="per_min"/>
            <maximalPower value="6176" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11677" unit="per_min"/>
            <maximalPower value="6831" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11999" unit="per_min"/>
            <maximalPower value="6417" unit="kW"/>
          </measurement>
        </maximalPowerMeasurements>
      </electricMotor>
      <electricMotor id="E_WARD">
        <energy_rate_fun_coeff_1 value="0.01"/>
        <energy_rate_fun_coeff_2 value="3.05236e-21"/>
        <energy_rate_fun_coeff_3 value="-3.14375e-25"/>
        <power_fun_coeff_1 value="-1823.18"/>
        <power_fun_coeff_2 value="1.10551"/>
        <power_fun_coeff_3 value="-3.4413e-05"/>
        <specificEnergyConsumptionMeasurements>
          <measurement>
            <compressorPower value="1311" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="2622" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="5520" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="6831" unit="kW"/>
            <fuelConsumption value="0.01" unit="kW"/>
          </measurement>
        </specificEnergyConsumptionMeasurements>
        <maximalPowerMeasurements>
          <measurement>
            <speed value="3419" unit="per_min"/>
            <maximalPower value="1311" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="4323" unit="per_min"/>
            <maximalPower value="2622" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="9161" unit="per_min"/>
            <maximalPower value="5520" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="10193" unit="per_min"/>
            <maximalPower value="5520" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11032" unit="per_min"/>
            <maximalPower value="6176" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11548" unit="per_min"/>
            <maximalPower value="6176" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11677" unit="per_min"/>
            <maximalPower value="6831" unit="kW"/>
          </measurement>
          <measurement>
            <speed value="11999" unit="per_min"/>
            <maximalPower value="6417" unit="kW"/>
          </measurement>
        </maximalPowerMeasurements>
      </electricMotor>
    </drives>
    <configurations>
      <configuration confId="p" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="1">
          <compressor id="T_WARD_P" nominalSpeed="11000"/>
        </stage>
      </configuration>
      <configuration confId="s" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="1">
          <compressor id="T_WARD_S" nominalSpeed="11000"/>
        </stage>
      </configuration>
    </configurations>
  </compressorStation>
  <compressorStation id="eN_BUND_OST">
    <compressors>
      <turboCompressor drive="P_M1502R_a" id="T_BUND_M1">
        <speedMin value="6174" unit="per_min"/>
        <speedMax value="10805" unit="per_min"/>
        <n_isoline_coeff_1 value="-13.631"/>
        <n_isoline_coeff_2 value="0.00537675"/>
        <n_isoline_coeff_3 value="-3.3997e-07"/>
        <n_isoline_coeff_4 value="4.10197"/>
        <n_isoline_coeff_5 value="0.00269417"/>
        <n_isoline_coeff_6 value="1.00907e-06"/>
        <n_isoline_coeff_7 value="-97.1125"/>
        <n_isoline_coeff_8 value="-0.000639406"/>
        <n_isoline_coeff_9 value="-3.55888e-07"/>
        <eta_ad_isoline_coeff_1 value="1.73736"/>
        <eta_ad_isoline_coeff_2 value="-0.000323609"/>
        <eta_ad_isoline_coeff_3 value="1.10225e-08"/>
        <eta_ad_isoline_coeff_4 value="-1.43259"/>
        <eta_ad_isoline_coeff_5 value="0.00123203"/>
        <eta_ad_isoline_coeff_6 value="-6.86927e-08"/>
        <eta_ad_isoline_coeff_7 value="-7.07265"/>
        <eta_ad_isoline_coeff_8 value="0.000203544"/>
        <eta_ad_isoline_coeff_9 value="1.60948e-08"/>
        <surgeline_coeff_1 value="-67.3513"/>
        <surgeline_coeff_2 value="398.19"/>
        <surgeline_coeff_3 value="-353.624"/>
        <chokeline_coeff_1 value="-20.545"/>
        <chokeline_coeff_2 value="53.1504"/>
        <chokeline_coeff_3 value="-2.46025"/>
        <efficiencyOfChokeline value="0.6"/>
        <settlelineMeasurements>
          <measurement>
            <speed value="6174" unit="per_min"/>
            <adiabaticHead value="14.4" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.27" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="7203" unit="per_min"/>
            <adiabaticHead value="19.9" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.3" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="7718" unit="per_min"/>
            <adiabaticHead value="23" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.32" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="8232" unit="per_min"/>
            <adiabaticHead value="26.2" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.33" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="8747" unit="per_min"/>
            <adiabaticHead value="29.7" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.35" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="9261" unit="per_min"/>
            <adiabaticHead value="33.3" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.38" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="9776" unit="per_min"/>
            <adiabaticHead value="36.8" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.42" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="10290" unit="per_min"/>
            <adiabaticHead value="40.4" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.46" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="10805" unit="per_min"/>
            <adiabaticHead value="44.2" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.51" unit="m_cube_per_s"/>
          </measurement>
        </settlelineMeasurements>
        <characteristicDiagramMeasurements>
          <adiabaticEfficiency value="0.75">
            <measurement>
              <speed value="6174" unit="per_min"/>
              <adiabaticHead value="14.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.28" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7203" unit="per_min"/>
              <adiabaticHead value="19.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.33" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7718" unit="per_min"/>
              <adiabaticHead value="22.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.36" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8232" unit="per_min"/>
              <adiabaticHead value="25.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.38" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8747" unit="per_min"/>
              <adiabaticHead value="28.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.41" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9261" unit="per_min"/>
              <adiabaticHead value="32.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.44" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9776" unit="per_min"/>
              <adiabaticHead value="36.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.46" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10290" unit="per_min"/>
              <adiabaticHead value="40.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.49" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <adiabaticHead value="44.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.51" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.76">
            <measurement>
              <speed value="6174" unit="per_min"/>
              <adiabaticHead value="13.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.33" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7203" unit="per_min"/>
              <adiabaticHead value="18.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.39" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7718" unit="per_min"/>
              <adiabaticHead value="21.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.42" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8232" unit="per_min"/>
              <adiabaticHead value="24.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.45" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8747" unit="per_min"/>
              <adiabaticHead value="27.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.47" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9261" unit="per_min"/>
              <adiabaticHead value="31.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9776" unit="per_min"/>
              <adiabaticHead value="34.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.53" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10290" unit="per_min"/>
              <adiabaticHead value="38.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.56" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <adiabaticHead value="42.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.59" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.765">
            <measurement>
              <speed value="6174" unit="per_min"/>
              <adiabaticHead value="12.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.38" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7203" unit="per_min"/>
              <adiabaticHead value="17.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.45" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7718" unit="per_min"/>
              <adiabaticHead value="19.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.48" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8232" unit="per_min"/>
              <adiabaticHead value="22.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.52" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8747" unit="per_min"/>
              <adiabaticHead value="25.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.55" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9261" unit="per_min"/>
              <adiabaticHead value="28.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.59" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9776" unit="per_min"/>
              <adiabaticHead value="32" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.63" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10290" unit="per_min"/>
              <adiabaticHead value="35.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.66" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <adiabaticHead value="39.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.7" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.76">
            <measurement>
              <speed value="6174" unit="per_min"/>
              <adiabaticHead value="11.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.41" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7203" unit="per_min"/>
              <adiabaticHead value="16.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.48" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7718" unit="per_min"/>
              <adiabaticHead value="18.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.52" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8232" unit="per_min"/>
              <adiabaticHead value="21.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.55" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8747" unit="per_min"/>
              <adiabaticHead value="24.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.59" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9261" unit="per_min"/>
              <adiabaticHead value="27.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.63" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9776" unit="per_min"/>
              <adiabaticHead value="30.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.66" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10290" unit="per_min"/>
              <adiabaticHead value="33.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.7" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <adiabaticHead value="37.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.74" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.75">
            <measurement>
              <speed value="6174" unit="per_min"/>
              <adiabaticHead value="13" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.36" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7203" unit="per_min"/>
              <adiabaticHead value="17.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.42" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7718" unit="per_min"/>
              <adiabaticHead value="20.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.45" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8232" unit="per_min"/>
              <adiabaticHead value="23.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.48" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8747" unit="per_min"/>
              <adiabaticHead value="26.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.52" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9261" unit="per_min"/>
              <adiabaticHead value="29.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.55" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9776" unit="per_min"/>
              <adiabaticHead value="33.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.58" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10290" unit="per_min"/>
              <adiabaticHead value="37" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.62" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <adiabaticHead value="40.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.65" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.74">
            <measurement>
              <speed value="6174" unit="per_min"/>
              <adiabaticHead value="11.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.42" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7203" unit="per_min"/>
              <adiabaticHead value="15.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7718" unit="per_min"/>
              <adiabaticHead value="17.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.54" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8232" unit="per_min"/>
              <adiabaticHead value="20.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.57" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8747" unit="per_min"/>
              <adiabaticHead value="23.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.61" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9261" unit="per_min"/>
              <adiabaticHead value="26.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.65" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9776" unit="per_min"/>
              <adiabaticHead value="29.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.68" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10290" unit="per_min"/>
              <adiabaticHead value="32.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.72" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <adiabaticHead value="35.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.76" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.72">
            <measurement>
              <speed value="6174" unit="per_min"/>
              <adiabaticHead value="10.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.44" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7203" unit="per_min"/>
              <adiabaticHead value="14.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.52" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7718" unit="per_min"/>
              <adiabaticHead value="16.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.56" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8232" unit="per_min"/>
              <adiabaticHead value="19.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.6" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8747" unit="per_min"/>
              <adiabaticHead value="21.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.64" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9261" unit="per_min"/>
              <adiabaticHead value="24.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.67" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9776" unit="per_min"/>
              <adiabaticHead value="27.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.71" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10290" unit="per_min"/>
              <adiabaticHead value="30.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.75" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <adiabaticHead value="33.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.79" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.7">
            <measurement>
              <speed value="6174" unit="per_min"/>
              <adiabaticHead value="9.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.46" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7203" unit="per_min"/>
              <adiabaticHead value="13.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.54" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7718" unit="per_min"/>
              <adiabaticHead value="15.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.57" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8232" unit="per_min"/>
              <adiabaticHead value="18.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.61" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8747" unit="per_min"/>
              <adiabaticHead value="20.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.65" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9261" unit="per_min"/>
              <adiabaticHead value="23.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.69" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9776" unit="per_min"/>
              <adiabaticHead value="26" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.73" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10290" unit="per_min"/>
              <adiabaticHead value="28.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.77" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <adiabaticHead value="32" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.81" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.6">
            <measurement>
              <speed value="6174" unit="per_min"/>
              <adiabaticHead value="7.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.49" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7203" unit="per_min"/>
              <adiabaticHead value="10.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.58" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7718" unit="per_min"/>
              <adiabaticHead value="12.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.62" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8232" unit="per_min"/>
              <adiabaticHead value="14.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.66" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8747" unit="per_min"/>
              <adiabaticHead value="16.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.71" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9261" unit="per_min"/>
              <adiabaticHead value="18.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.75" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9776" unit="per_min"/>
              <adiabaticHead value="20.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.79" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10290" unit="per_min"/>
              <adiabaticHead value="22.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.83" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <adiabaticHead value="25.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.87" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
        </characteristicDiagramMeasurements>
      </turboCompressor>
      <turboCompressor drive="P_M1502_2" id="T_BUND_M2">
        <speedMin value="6174" unit="per_min"/>
        <speedMax value="10805" unit="per_min"/>
        <n_isoline_coeff_1 value="-47.1332"/>
        <n_isoline_coeff_2 value="0.0151348"/>
        <n_isoline_coeff_3 value="-9.84287e-07"/>
        <n_isoline_coeff_4 value="52.5022"/>
        <n_isoline_coeff_5 value="-0.0139616"/>
        <n_isoline_coeff_6 value="1.7035e-06"/>
        <n_isoline_coeff_7 value="-42.3544"/>
        <n_isoline_coeff_8 value="0.00395533"/>
        <n_isoline_coeff_9 value="-4.62231e-07"/>
        <eta_ad_isoline_coeff_1 value="1.66631"/>
        <eta_ad_isoline_coeff_2 value="-0.000228832"/>
        <eta_ad_isoline_coeff_3 value="2.34376e-09"/>
        <eta_ad_isoline_coeff_4 value="-1.3591"/>
        <eta_ad_isoline_coeff_5 value="0.00067716"/>
        <eta_ad_isoline_coeff_6 value="-3.00171e-08"/>
        <eta_ad_isoline_coeff_7 value="-1.76341"/>
        <eta_ad_isoline_coeff_8 value="1.43401e-05"/>
        <eta_ad_isoline_coeff_9 value="4.33303e-09"/>
        <surgeline_coeff_1 value="-18.1765"/>
        <surgeline_coeff_2 value="81.7345"/>
        <surgeline_coeff_3 value="-4.5301"/>
        <chokeline_coeff_1 value="0.292778"/>
        <chokeline_coeff_2 value="-5.66314"/>
        <chokeline_coeff_3 value="14.4081"/>
        <efficiencyOfChokeline value="0.6"/>
        <settlelineMeasurements>
          <measurement>
            <speed value="6174" unit="per_min"/>
            <adiabaticHead value="16" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.43" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="7203" unit="per_min"/>
            <adiabaticHead value="21.8" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.5" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="8232" unit="per_min"/>
            <adiabaticHead value="28.4" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.59" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="9261" unit="per_min"/>
            <adiabaticHead value="36" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.69" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="10290" unit="per_min"/>
            <adiabaticHead value="44.3" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.8" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="10805" unit="per_min"/>
            <adiabaticHead value="48.8" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.86" unit="m_cube_per_s"/>
          </measurement>
        </settlelineMeasurements>
        <characteristicDiagramMeasurements>
          <adiabaticEfficiency value="0.8">
            <measurement>
              <speed value="6174" unit="per_min"/>
              <adiabaticHead value="15.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.53" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7203" unit="per_min"/>
              <adiabaticHead value="21.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.6" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8232" unit="per_min"/>
              <adiabaticHead value="28" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.68" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9261" unit="per_min"/>
              <adiabaticHead value="35.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.77" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10290" unit="per_min"/>
              <adiabaticHead value="44" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.87" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <adiabaticHead value="48.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.92" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.82">
            <measurement>
              <speed value="6174" unit="per_min"/>
              <adiabaticHead value="0" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7203" unit="per_min"/>
              <adiabaticHead value="0" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8232" unit="per_min"/>
              <adiabaticHead value="26.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.81" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9261" unit="per_min"/>
              <adiabaticHead value="34.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.86" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10290" unit="per_min"/>
              <adiabaticHead value="43.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.94" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <adiabaticHead value="47.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.82">
            <measurement>
              <speed value="6174" unit="per_min"/>
              <adiabaticHead value="0" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7203" unit="per_min"/>
              <adiabaticHead value="0" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8232" unit="per_min"/>
              <adiabaticHead value="26.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.81" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9261" unit="per_min"/>
              <adiabaticHead value="33" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.97" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10290" unit="per_min"/>
              <adiabaticHead value="40.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.14" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <adiabaticHead value="44.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.22" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.8">
            <measurement>
              <speed value="6174" unit="per_min"/>
              <adiabaticHead value="13.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.66" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7203" unit="per_min"/>
              <adiabaticHead value="18.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.8" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8232" unit="per_min"/>
              <adiabaticHead value="24.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.94" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9261" unit="per_min"/>
              <adiabaticHead value="30.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.08" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10290" unit="per_min"/>
              <adiabaticHead value="37.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.22" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <adiabaticHead value="41.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.3" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.75">
            <measurement>
              <speed value="6174" unit="per_min"/>
              <adiabaticHead value="11.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.77" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7203" unit="per_min"/>
              <adiabaticHead value="16.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.9" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8232" unit="per_min"/>
              <adiabaticHead value="21.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.04" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9261" unit="per_min"/>
              <adiabaticHead value="27.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.18" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10290" unit="per_min"/>
              <adiabaticHead value="34.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.32" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <adiabaticHead value="37.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.39" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.7">
            <measurement>
              <speed value="6174" unit="per_min"/>
              <adiabaticHead value="10.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.83" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7203" unit="per_min"/>
              <adiabaticHead value="14.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.96" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8232" unit="per_min"/>
              <adiabaticHead value="19" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.11" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9261" unit="per_min"/>
              <adiabaticHead value="24.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.25" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10290" unit="per_min"/>
              <adiabaticHead value="30.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.39" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <adiabaticHead value="34.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.46" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.65">
            <measurement>
              <speed value="6174" unit="per_min"/>
              <adiabaticHead value="9.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.87" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7203" unit="per_min"/>
              <adiabaticHead value="12.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.02" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8232" unit="per_min"/>
              <adiabaticHead value="16.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.17" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9261" unit="per_min"/>
              <adiabaticHead value="21.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.31" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10290" unit="per_min"/>
              <adiabaticHead value="27.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.45" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <adiabaticHead value="30.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.52" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.6">
            <measurement>
              <speed value="6174" unit="per_min"/>
              <adiabaticHead value="8.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.91" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="7203" unit="per_min"/>
              <adiabaticHead value="10.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.06" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="8232" unit="per_min"/>
              <adiabaticHead value="14.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.22" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9261" unit="per_min"/>
              <adiabaticHead value="18.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.36" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10290" unit="per_min"/>
              <adiabaticHead value="23.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <adiabaticHead value="27" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.57" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
        </characteristicDiagramMeasurements>
      </turboCompressor>
<!-- Leistungsstaerkste Maschine
      <turboCompressor drive="P_CENTAURH" id="T_BUND_M3">
        <speedMin value="7865" unit="per_min"/>
        <speedMax value="16517" unit="per_min"/>
        <n_isoline_coeff_1 value="119.551"/>
        <n_isoline_coeff_2 value="-0.0201609"/>
        <n_isoline_coeff_3 value="7.98766e-07"/>
        <n_isoline_coeff_4 value="-185.978"/>
        <n_isoline_coeff_5 value="0.0383048"/>
        <n_isoline_coeff_6 value="-1.36735e-06"/>
        <n_isoline_coeff_7 value="15.0967"/>
        <n_isoline_coeff_8 value="-0.0101916"/>
        <n_isoline_coeff_9 value="4.4774e-07"/>
        <eta_ad_isoline_coeff_1 value="1.23404"/>
        <eta_ad_isoline_coeff_2 value="-5.70441e-05"/>
        <eta_ad_isoline_coeff_3 value="-7.48678e-09"/>
        <eta_ad_isoline_coeff_4 value="-1.10636"/>
        <eta_ad_isoline_coeff_5 value="0.000459166"/>
        <eta_ad_isoline_coeff_6 value="-1.11521e-08"/>
        <eta_ad_isoline_coeff_7 value="-1.66039"/>
        <eta_ad_isoline_coeff_8 value="-4.04975e-07"/>
        <eta_ad_isoline_coeff_9 value="1.94841e-09"/>
        <surgeline_coeff_1 value="-34.875"/>
        <surgeline_coeff_2 value="113.871"/>
        <surgeline_coeff_3 value="-37.715"/>
        <chokeline_coeff_1 value="-3.43425"/>
        <chokeline_coeff_2 value="-1.45166"/>
        <chokeline_coeff_3 value="8.80885"/>
        <efficiencyOfChokeline value="0.6"/>
        <settlelineMeasurements>
          <measurement>
            <speed value="7865" unit="per_min"/>
            <adiabaticHead value="12" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.5" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="9438" unit="per_min"/>
            <adiabaticHead value="17.4" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.56" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="11011" unit="per_min"/>
            <adiabaticHead value="23.4" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.64" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="12584" unit="per_min"/>
            <adiabaticHead value="30.4" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.78" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="14157" unit="per_min"/>
            <adiabaticHead value="38.1" unit="kJ_per_kg"/>
            <volumetricFlowrate value="0.92" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="15730" unit="per_min"/>
            <adiabaticHead value="46.2" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.16" unit="m_cube_per_s"/>
          </measurement>
          <measurement>
            <speed value="16517" unit="per_min"/>
            <adiabaticHead value="49.9" unit="kJ_per_kg"/>
            <volumetricFlowrate value="1.32" unit="m_cube_per_s"/>
          </measurement>
        </settlelineMeasurements>
        <characteristicDiagramMeasurements>
          <adiabaticEfficiency value="0.84">
            <measurement>
              <speed value="7865" unit="per_min"/>
              <adiabaticHead value="15.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.72" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9438" unit="per_min"/>
              <adiabaticHead value="17.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.68" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11011" unit="per_min"/>
              <adiabaticHead value="23.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.73" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="12584" unit="per_min"/>
              <adiabaticHead value="30.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.85" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="14157" unit="per_min"/>
              <adiabaticHead value="37.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.02" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="15730" unit="per_min"/>
              <adiabaticHead value="44.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.32" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="16517" unit="per_min"/>
              <adiabaticHead value="44.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.44" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.84">
            <measurement>
              <speed value="7865" unit="per_min"/>
              <adiabaticHead value="15.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.72" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9438" unit="per_min"/>
              <adiabaticHead value="16.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.81" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11011" unit="per_min"/>
              <adiabaticHead value="21.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.99" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="12584" unit="per_min"/>
              <adiabaticHead value="27.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.17" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="14157" unit="per_min"/>
              <adiabaticHead value="33.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.33" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="15730" unit="per_min"/>
              <adiabaticHead value="41.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.46" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="16517" unit="per_min"/>
              <adiabaticHead value="44.1" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.44" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.83">
            <measurement>
              <speed value="7865" unit="per_min"/>
              <adiabaticHead value="11" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.7" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9438" unit="per_min"/>
              <adiabaticHead value="14.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.92" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11011" unit="per_min"/>
              <adiabaticHead value="19.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.1" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="12584" unit="per_min"/>
              <adiabaticHead value="24.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.27" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="14157" unit="per_min"/>
              <adiabaticHead value="30.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.43" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="15730" unit="per_min"/>
              <adiabaticHead value="38.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.58" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="16517" unit="per_min"/>
              <adiabaticHead value="42.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.65" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.8">
            <measurement>
              <speed value="7865" unit="per_min"/>
              <adiabaticHead value="9.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.79" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9438" unit="per_min"/>
              <adiabaticHead value="13.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.99" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11011" unit="per_min"/>
              <adiabaticHead value="17.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.17" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="12584" unit="per_min"/>
              <adiabaticHead value="23" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.34" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="14157" unit="per_min"/>
              <adiabaticHead value="28.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.5" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="15730" unit="per_min"/>
              <adiabaticHead value="35.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.67" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="16517" unit="per_min"/>
              <adiabaticHead value="39" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.75" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.75">
            <measurement>
              <speed value="7865" unit="per_min"/>
              <adiabaticHead value="8.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.85" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9438" unit="per_min"/>
              <adiabaticHead value="11.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.04" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11011" unit="per_min"/>
              <adiabaticHead value="16" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.22" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="12584" unit="per_min"/>
              <adiabaticHead value="20.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.4" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="14157" unit="per_min"/>
              <adiabaticHead value="26.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.57" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="15730" unit="per_min"/>
              <adiabaticHead value="32.3" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.74" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="16517" unit="per_min"/>
              <adiabaticHead value="35.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.83" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.7">
            <measurement>
              <speed value="7865" unit="per_min"/>
              <adiabaticHead value="7.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.91" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9438" unit="per_min"/>
              <adiabaticHead value="10.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.09" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11011" unit="per_min"/>
              <adiabaticHead value="14.4" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.27" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="12584" unit="per_min"/>
              <adiabaticHead value="18.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.45" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="14157" unit="per_min"/>
              <adiabaticHead value="23.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.62" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="15730" unit="per_min"/>
              <adiabaticHead value="29.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.8" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="16517" unit="per_min"/>
              <adiabaticHead value="32.9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.88" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.65">
            <measurement>
              <speed value="7865" unit="per_min"/>
              <adiabaticHead value="6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.95" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9438" unit="per_min"/>
              <adiabaticHead value="9" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.13" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11011" unit="per_min"/>
              <adiabaticHead value="12.6" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.31" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="12584" unit="per_min"/>
              <adiabaticHead value="16.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.49" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="14157" unit="per_min"/>
              <adiabaticHead value="21.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.67" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="15730" unit="per_min"/>
              <adiabaticHead value="27.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.84" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="16517" unit="per_min"/>
              <adiabaticHead value="30.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.92" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
          <adiabaticEfficiency value="0.6">
            <measurement>
              <speed value="7865" unit="per_min"/>
              <adiabaticHead value="5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="0.98" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="9438" unit="per_min"/>
              <adiabaticHead value="7.8" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.16" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="11011" unit="per_min"/>
              <adiabaticHead value="11.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.34" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="12584" unit="per_min"/>
              <adiabaticHead value="15.2" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.52" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="14157" unit="per_min"/>
              <adiabaticHead value="20" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.7" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="15730" unit="per_min"/>
              <adiabaticHead value="25.5" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.87" unit="m_cube_per_s"/>
            </measurement>
            <measurement>
              <speed value="16517" unit="per_min"/>
              <adiabaticHead value="28.7" unit="kJ_per_kg"/>
              <volumetricFlowrate value="1.96" unit="m_cube_per_s"/>
            </measurement>
          </adiabaticEfficiency>
        </characteristicDiagramMeasurements>
      </turboCompressor>
-->
    </compressors>
    <drives>
      <gasTurbine id="P_M1502R_a">
        <energy_rate_fun_coeff_1 value="1950"/>
        <energy_rate_fun_coeff_2 value="2.75"/>
        <energy_rate_fun_coeff_3 value="2.27374e-19"/>
        <power_fun_coeff_1 value="75.0969"/>
        <power_fun_coeff_2 value="0.671444"/>
        <power_fun_coeff_3 value="-3.00974e-05"/>
        <power_fun_coeff_4 value="0.0189513"/>
        <power_fun_coeff_5 value="-0.00422203"/>
        <power_fun_coeff_6 value="1.88527e-07"/>
        <power_fun_coeff_7 value="-0.0366493"/>
        <power_fun_coeff_8 value="1.19207e-05"/>
        <power_fun_coeff_9 value="-5.35589e-10"/>
        <specificEnergyConsumptionMeasurements>
          <measurement>
            <compressorPower value="2000" unit="kW"/>
            <fuelConsumption value="7450" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="2500" unit="kW"/>
            <fuelConsumption value="8825" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="3000" unit="kW"/>
            <fuelConsumption value="10200" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="3500" unit="kW"/>
            <fuelConsumption value="11575" unit="kW"/>
          </measurement>
        </specificEnergyConsumptionMeasurements>
        <maximalPowerMeasurements>
          <ambientTemperature value="-5" unit="C">
            <measurement>
              <speed value="7564" unit="per_min"/>
              <maximalPower value="3538" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="8644" unit="per_min"/>
              <maximalPower value="3743" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="9725" unit="per_min"/>
              <maximalPower value="3875" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <maximalPower value="3935" unit="kW"/>
            </measurement>
          </ambientTemperature>
          <ambientTemperature value="5" unit="C">
            <measurement>
              <speed value="7564" unit="per_min"/>
              <maximalPower value="3327" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="8644" unit="per_min"/>
              <maximalPower value="3519" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="9725" unit="per_min"/>
              <maximalPower value="3643" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <maximalPower value="3699" unit="kW"/>
            </measurement>
          </ambientTemperature>
          <ambientTemperature value="15" unit="C">
            <measurement>
              <speed value="7564" unit="per_min"/>
              <maximalPower value="3120" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="8644" unit="per_min"/>
              <maximalPower value="3300" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="9725" unit="per_min"/>
              <maximalPower value="3417" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <maximalPower value="3469" unit="kW"/>
            </measurement>
          </ambientTemperature>
          <ambientTemperature value="25" unit="C">
            <measurement>
              <speed value="7564" unit="per_min"/>
              <maximalPower value="2918" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="8644" unit="per_min"/>
              <maximalPower value="3087" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="9725" unit="per_min"/>
              <maximalPower value="3196" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="10805" unit="per_min"/>
              <maximalPower value="3245" unit="kW"/>
            </measurement>
          </ambientTemperature>
        </maximalPowerMeasurements>
      </gasTurbine>
      <gasTurbine id="P_M1502_2">
        <energy_rate_fun_coeff_1 value="4500"/>
        <energy_rate_fun_coeff_2 value="2.78"/>
        <energy_rate_fun_coeff_3 value="-2.90417e-18"/>
        <power_fun_coeff_1 value="76.5281"/>
        <power_fun_coeff_2 value="0.671473"/>
        <power_fun_coeff_3 value="-3.01113e-05"/>
        <power_fun_coeff_4 value="0.01"/>
        <power_fun_coeff_5 value="-0.00422222"/>
        <power_fun_coeff_6 value="1.88615e-07"/>
        <power_fun_coeff_7 value="-0.036625"/>
        <power_fun_coeff_8 value="1.19213e-05"/>
        <power_fun_coeff_9 value="-5.35837e-10"/>
        <specificEnergyConsumptionMeasurements>
          <measurement>
            <compressorPower value="2000" unit="kW"/>
            <fuelConsumption value="10060" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="2500" unit="kW"/>
            <fuelConsumption value="11450" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="3000" unit="kW"/>
            <fuelConsumption value="12840" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="3650" unit="kW"/>
            <fuelConsumption value="14647" unit="kW"/>
          </measurement>
        </specificEnergyConsumptionMeasurements>
        <maximalPowerMeasurements>
          <ambientTemperature value="-5" unit="C">
            <measurement>
              <speed value="7560" unit="per_min"/>
              <maximalPower value="3538" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="8640" unit="per_min"/>
              <maximalPower value="3743" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="9720" unit="per_min"/>
              <maximalPower value="3875" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="10800" unit="per_min"/>
              <maximalPower value="3935" unit="kW"/>
            </measurement>
          </ambientTemperature>
          <ambientTemperature value="5" unit="C">
            <measurement>
              <speed value="7560" unit="per_min"/>
              <maximalPower value="3327" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="8640" unit="per_min"/>
              <maximalPower value="3519" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="9720" unit="per_min"/>
              <maximalPower value="3643" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="10800" unit="per_min"/>
              <maximalPower value="3699" unit="kW"/>
            </measurement>
          </ambientTemperature>
          <ambientTemperature value="15" unit="C">
            <measurement>
              <speed value="7560" unit="per_min"/>
              <maximalPower value="3120" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="8640" unit="per_min"/>
              <maximalPower value="3300" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="9720" unit="per_min"/>
              <maximalPower value="3417" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="10800" unit="per_min"/>
              <maximalPower value="3469" unit="kW"/>
            </measurement>
          </ambientTemperature>
          <ambientTemperature value="25" unit="C">
            <measurement>
              <speed value="7560" unit="per_min"/>
              <maximalPower value="2918" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="8640" unit="per_min"/>
              <maximalPower value="3087" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="9720" unit="per_min"/>
              <maximalPower value="3196" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="10800" unit="per_min"/>
              <maximalPower value="3245" unit="kW"/>
            </measurement>
          </ambientTemperature>
        </maximalPowerMeasurements>
      </gasTurbine>
<!-- Antrieb einer leistungsstaerksten Maschine
      <gasTurbine id="P_CENTAURH">
        <energy_rate_fun_coeff_1 value="2900"/>
        <energy_rate_fun_coeff_2 value="2.75"/>
        <energy_rate_fun_coeff_3 value="-7.23117e-19"/>
        <power_fun_coeff_1 value="24.0048"/>
        <power_fun_coeff_2 value="0.503065"/>
        <power_fun_coeff_3 value="-1.45582e-05"/>
        <power_fun_coeff_4 value="1.01776"/>
        <power_fun_coeff_5 value="-0.00376062"/>
        <power_fun_coeff_6 value="1.09959e-07"/>
        <power_fun_coeff_7 value="-0.0874826"/>
        <power_fun_coeff_8 value="1.28639e-05"/>
        <power_fun_coeff_9 value="-4.58164e-10"/>
        <specificEnergyConsumptionMeasurements>
          <measurement>
            <compressorPower value="4100" unit="kW"/>
            <fuelConsumption value="14175" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="3500" unit="kW"/>
            <fuelConsumption value="12525" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="3000" unit="kW"/>
            <fuelConsumption value="11150" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="2500" unit="kW"/>
            <fuelConsumption value="9775" unit="kW"/>
          </measurement>
          <measurement>
            <compressorPower value="2000" unit="kW"/>
            <fuelConsumption value="8400" unit="kW"/>
          </measurement>
        </specificEnergyConsumptionMeasurements>
        <maximalPowerMeasurements>
          <ambientTemperature value="-5" unit="C">
            <measurement>
              <speed value="11561" unit="per_min"/>
              <maximalPower value="4033" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="13213" unit="per_min"/>
              <maximalPower value="4277" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="14864" unit="per_min"/>
              <maximalPower value="4438" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="16516" unit="per_min"/>
              <maximalPower value="4517" unit="kW"/>
            </measurement>
          </ambientTemperature>
          <ambientTemperature value="5" unit="C">
            <measurement>
              <speed value="11561" unit="per_min"/>
              <maximalPower value="3755" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="13213" unit="per_min"/>
              <maximalPower value="3982" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="14864" unit="per_min"/>
              <maximalPower value="4132" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="16516" unit="per_min"/>
              <maximalPower value="4206" unit="kW"/>
            </measurement>
          </ambientTemperature>
          <ambientTemperature value="15" unit="C">
            <measurement>
              <speed value="11561" unit="per_min"/>
              <maximalPower value="3478" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="13213" unit="per_min"/>
              <maximalPower value="3688" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="14864" unit="per_min"/>
              <maximalPower value="3827" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="16516" unit="per_min"/>
              <maximalPower value="3895" unit="kW"/>
            </measurement>
          </ambientTemperature>
          <ambientTemperature value="25" unit="C">
            <measurement>
              <speed value="11561" unit="per_min"/>
              <maximalPower value="3200" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="13213" unit="per_min"/>
              <maximalPower value="3394" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="14864" unit="per_min"/>
              <maximalPower value="3522" unit="kW"/>
            </measurement>
            <measurement>
              <speed value="16516" unit="per_min"/>
              <maximalPower value="3584" unit="kW"/>
            </measurement>
          </ambientTemperature>
        </maximalPowerMeasurements>
      </gasTurbine>
-->
    </drives>
    <configurations>
      <configuration confId="1" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="1">
          <compressor id="T_BUND_M1" nominalSpeed="6380"/>
        </stage>
      </configuration>
      <configuration confId="2" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="1">
          <compressor id="T_BUND_M2" nominalSpeed="6380"/>
        </stage>
      </configuration>
<!-- not active (Info: Uwe Gotzes, 14. Juni 2010)
      <configuration confId="3" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="1">
          <compressor id="T_BUND_M3" nominalSpeed="14154"/>
        </stage>
      </configuration>
-->
<!-- not active (Info: Uwe Gotzes, 14. Juni 2010)
      <configuration confId="S12" nrOfSerialStages="2">
        <stage stageNr="1" nrOfParallelUnits="1">
          <compressor id="T_BUND_M1" nominalSpeed="6380"/>
        </stage>
        <stage stageNr="2" nrOfParallelUnits="1">
          <compressor id="T_BUND_M2" nominalSpeed="6380"/>
        </stage>
      </configuration>
-->
      <configuration confId="P12" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="2">
          <compressor id="T_BUND_M1" nominalSpeed="6380"/>
          <compressor id="T_BUND_M2" nominalSpeed="6380"/>
        </stage>
      </configuration>
<!-- not active (Info: Uwe Gotzes, 14. Juni 2010)
      <configuration confId="S13" nrOfSerialStages="2">
        <stage stageNr="1" nrOfParallelUnits="1">
          <compressor id="T_BUND_M1" nominalSpeed="6380"/>
        </stage>
        <stage stageNr="2" nrOfParallelUnits="1">
          <compressor id="T_BUND_M3" nominalSpeed="14154"/>
        </stage>
      </configuration>
-->
<!-- not active (Info: Uwe Gotzes, 14. Juni 2010)
      <configuration confId="S23" nrOfSerialStages="2">
        <stage stageNr="1" nrOfParallelUnits="1">
          <compressor id="T_BUND_M2" nominalSpeed="6380"/>
        </stage>
        <stage stageNr="2" nrOfParallelUnits="1">
          <compressor id="T_BUND_M3" nominalSpeed="14154"/>
        </stage>
      </configuration>
-->
<!-- not active (Info: Uwe Gotzes, 14. Juni 2010)
      <configuration confId="P13" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="2">
          <compressor id="T_BUND_M1" nominalSpeed="6380"/>
          <compressor id="T_BUND_M3" nominalSpeed="14154"/>
        </stage>
      </configuration>
-->
<!-- not active (Info: Uwe Gotzes, 14. Juni 2010)
      <configuration confId="P23" nrOfSerialStages="1">
        <stage stageNr="1" nrOfParallelUnits="2">
          <compressor id="T_BUND_M2" nominalSpeed="6380"/>
          <compressor id="T_BUND_M3" nominalSpeed="14154"/>
        </stage>
      </configuration>
-->
    </configurations>
  </compressorStation>
</compressorStations>
