<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="../stylesheet/view.xsl"?>
<combinedDecisions xmlns="http://www.am.uni-erlangen.de/wima/CombinedDecisions/XMLSchema"
      xmlns:framework="http://www.am.uni-erlangen.de/wima/CombinedDecisions/XMLSchema"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.am.uni-erlangen.de/wima/CombinedDecisions/XMLSchema ../../../schema/CombinedDecisions.xsd">
  <decisionGroup id="WOLTZETEN">
    <sourceComment>Decision Group fuer die Reglerstation Woltzeten von Armin Fuegenschuh (ZIB), 29.08.2010</sourceComment>
    <decision id="d1">
      <controlValve id="eN_WOLT_ETZE" value="1" flowDirection="1"/>
      <controlValve id="eN_CV_ETZ_WOLTZ" value="0"/>
    </decision>
    <decision id="d2">
      <controlValve id="eN_WOLT_ETZE" value="0"/>
      <controlValve id="eN_CV_ETZ_WOLTZ" value="1" flowDirection="1"/>
    </decision>
    <decision id="d3">
      <controlValve id="eN_WOLT_ETZE" value="0"/>
      <controlValve id="eN_CV_ETZ_WOLTZ" value="0"/>
    </decision>    
  </decisionGroup>
  <decisionGroup id="BUNDE">
    <sourceComment>Decision Group fuer die Verdichterstation Bunde von Uwe Gotzes (OGE) und Armin Fuegenschuh (ZIB), 23.08.2010</sourceComment>
    <decision id="d1" fullName="Von Ost nach West unverdichtet">
       <compressorStation id="eN_BUND_OST" value="0"/>
       <valve id="eN_VA_020" value="0"/>
       <valve id="eN_VA_021" value="0"/>
       <valve id="eN_VA_026" value="0"/>
       <valve id="eN_VA_029" value="1" flowDirection="0"/>
    </decision>
    <decision id="d2" fullName="Von West nach Ost verdichtet">
       <compressorStation id="eN_BUND_OST" value="1" flowDirection="0"/>
       <valve id="eN_VA_020" value="1" flowDirection="1"/>
       <valve id="eN_VA_021" value="1" flowDirection="1"/>
       <valve id="eN_VA_026" value="0"/>
       <valve id="eN_VA_029" value="0"/>
    </decision>
    <decision id="d3" fullName="Von West nach Ost unverdichtet">
       <compressorStation id="eN_BUND_OST" value="0"/>
       <valve id="eN_VA_020" value="0"/>
       <valve id="eN_VA_021" value="1" flowDirection="1"/>
       <valve id="eN_VA_026" value="1" flowDirection="1"/>
       <valve id="eN_VA_029" value="0"/>
    </decision>
  </decisionGroup>
  <decisionGroup id="WARDENBURG">
    <sourceComment>Decision Group fuer die Station WARDenburg von Uwe Gotzes (OGE) und Armin Fuegenschuh (ZIB), 09.09.2010</sourceComment>
    <decision id="d1_1" fullName="1.1 Ueberstroemen von Dornum nach Drohne">
      <compressorStation id="eN_WARD_ME_1" value="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="0"/>
      <controlValve id="eN_WARD_RS_1" value="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="0"/>
      <valve id="eN_WARD_VA_02" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>
    <decision id="d1_2" fullName="1.2 Ueberstromen von Dornum nach Achim">
      <compressorStation id="eN_WARD_ME_1" value="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d1_3" fullName="1.3 Ueberstroemen von Achim nach Drohne">
      <compressorStation id="eN_WARD_ME_1" value="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d1_4" fullName="1.4 Ueberstroemen von Dornum nach Drohne + Ueberstroemen von Dornum nach Achim">
      <compressorStation id="eN_WARD_ME_1" value="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_01" value="0"/>
      <valve id="eN_WARD_VA_02" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d1_5" fullName="1.5 Ueberstroemen von Dornum nach Drohne + Ueberstroemen von Achim nach Drohne">
      <compressorStation id="eN_WARD_ME_1" value="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="0"/>
      <valve id="eN_WARD_VA_02" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d2_1_1" fullName="2.1 Verdichten von Dornum nach Drohne (1)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="0"/>
      <controlValve id="eN_WARD_RS_1" value="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d2_1_2" fullName="2.1 Verdichten von Dornum nach Drohne (2)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="0"/>
      <controlValve id="eN_WARD_RS_1" value="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d2_1_3" fullName="2.1 Verdichten von Dornum nach Drohne (3)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="0"/>
      <controlValve id="eN_WARD_RS_1" value="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d2_1_4" fullName="2.1 Verdichten von Dornum nach Drohne (4)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d2_2_1" fullName="2.2 Verdichten von Dornum nach Achim (1)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d2_2_2" fullName="2.2 Verdichten von Dornum nach Achim (2)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d2_2_3" fullName="2.2 Verdichten von Dornum nach Achim (3)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d2_2_4" fullName="2.2 Verdichten von Dornum nach Achim (4)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d2_3a_1" fullName="2.3a Verdichten von Achim nach Drohne (1)">
      <compressorStation id="eN_WARD_ME_1" value="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d2_3a_2" fullName="2.3a Verdichten von Achim nach Drohne (2)">
      <compressorStation id="eN_WARD_ME_1" value="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d2_3a_3" fullName="2.3a Verdichten von Achim nach Drohne (3)">
      <compressorStation id="eN_WARD_ME_1" value="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d2_3a_4" fullName="2.3a Verdichten von Achim nach Drohne (4)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d2_3b_2s" fullName="2.3b Verdichten von Achim nach Drohne (2 in Serie)">
      <compressorStation id="eN_WARD_ME_1" value="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d2_4a_1g" fullName="2.4a Verdichten von Dornum nach Drohne + Verdichten von Dornum nach Achim (1, gemeinsam)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d2_4a_2g" fullName="2.4a Verdichten von Dornum nach Drohne + Verdichten von Dornum nach Achim (2, gemeinsam)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d2_4a_3g" fullName="2.4a Verdichten von Dornum nach Drohne + Verdichten von Dornum nach Achim (3, gemeinsam)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d2_4a_4g" fullName="2.4a Verdichten von Dornum nach Drohne + Verdichten von Dornum nach Achim (4, gemeinsam)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d2_4b_1_1" fullName="2.4b Verdichten von Dornum nach Drohne (1) + Verdichten von Dornum nach Achim (1)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d2_4b_1_2" fullName="2.4b Verdichten von Dornum nach Drohne (1) + Verdichten von Dornum nach Achim (2)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d2_4b_1_3" fullName="2.4b Verdichten von Dornum nach Drohne (1) + Verdichten von Dornum nach Achim (3)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d2_4b_2_1" fullName="2.4b Verdichten von Dornum nach Drohne (2) + Verdichten von Dornum nach Achim (1)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d2_4b_3_1" fullName="2.4b Verdichten von Dornum nach Drohne (3) + Verdichten von Dornum nach Achim (1)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d2_4b_2_2" fullName="2.4b Verdichten von Dornum nach Drohne (2) + Verdichten von Dornum nach Achim (2)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d2_5a_1_1" fullName="2.5a Verdichten von Dornum nach Drohne (1) + Verdichten von Achim nach Drohne (1)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d2_5a_1_2" fullName="2.5a Verdichten von Dornum nach Drohne (1) + Verdichten von Achim nach Drohne (2)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d2_5a_1_3" fullName="2.5a Verdichten von Dornum nach Drohne (1) + Verdichten von Achim nach Drohne (3)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d2_5a_2_1" fullName="2.5a Verdichten von Dornum nach Drohne (2) + Verdichten von Achim nach Drohne (1)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d2_5a_3_1" fullName="2.5a Verdichten von Dornum nach Drohne (3) + Verdichten von Achim nach Drohne (1)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d2_5a_2_2" fullName="2.5a Verdichten von Dornum nach Drohne (2) + Verdichten von Achim nach Drohne (2)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d2_5b_1_1" fullName="2.5b Verdichten von Dornum nach Drohne (1) + Verdichten von Achim nach Drohne (1, auf Saugseite)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d2_5b_1_2" fullName="2.5b Verdichten von Dornum nach Drohne (1) + Verdichten von Achim nach Drohne (2, auf Saugseite)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d2_5b_1_3" fullName="2.5b Verdichten von Dornum nach Drohne (1) + Verdichten von Achim nach Drohne (3, auf Saugseite)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d2_5b_2_1" fullName="2.5b Verdichten von Dornum nach Drohne (2) + Verdichten von Achim nach Drohne (1, auf Saugseite)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1"/>
      <valve id="eN_WARD_VA_07" value="0"/>
      <valve id="eN_WARD_VA_08" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d2_5b_3_1" fullName="2.5b Verdichten von Dornum nach Drohne (3) + Verdichten von Achim nach Drohne (1, auf Saugseite)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1"/>
      <valve id="eN_WARD_VA_10" value="0"/>
      <valve id="eN_WARD_VA_11" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d2_5b_2_2" fullName="2.5b Verdichten von Dornum nach Drohne (2) + Verdichten von Achim nach Drohne (2, auf Saugseite)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1"/>
      <valve id="eN_WARD_VA_07" value="0"/>
      <valve id="eN_WARD_VA_08" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d3_4a_1" fullName="3.4a Ueberstroemen von Dornum nach Drohne + Verdichten von Dornum nach Achim (1)">
      <compressorStation id="eN_WARD_ME_1" value="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d3_4a_2" fullName="3.4a Ueberstroemen von Dornum nach Drohne + Verdichten von Dornum nach Achim (2)">
      <compressorStation id="eN_WARD_ME_1" value="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d3_4a_3" fullName="3.4a Ueberstroemen von Dornum nach Drohne + Verdichten von Dornum nach Achim (3)">
      <compressorStation id="eN_WARD_ME_1" value="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d3_4a_4" fullName="3.4a Ueberstroemen von Dornum nach Drohne + Verdichten von Dornum nach Achim (4)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="0"/>
      <valve id="eN_WARD_VA_17" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d3_4b_1" fullName="3.4b Verdichten von Dornum nach Drohne (1) + Ueberstroemen von Dornum nach Achim">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d3_4b_2" fullName="3.4b Verdichten von Dornum nach Drohne (2) + Ueberstroemen von Dornum nach Achim">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d3_4b_3" fullName="3.4b Verdichten von Dornum nach Drohne (3) + Ueberstroemen von Dornum nach Achim">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d3_4b_4" fullName="3.4b Verdichten von Dornum nach Drohne (4) + Ueberstroemen von Dornum nach Achim">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_02" value="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="0"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_19" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_20" value="0"/>
    </decision>    
    <decision id="d3_5a_1" fullName="3.5a Ueberstroemen von Dornum nach Drohne + Verdichten von Achim nach Drohne (1)">
      <compressorStation id="eN_WARD_ME_1" value="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="0"/>
      <valve id="eN_WARD_VA_02" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d3_5a_2" fullName="3.5a Ueberstroemen von Dornum nach Drohne + Verdichten von Achim nach Drohne (2)">
      <compressorStation id="eN_WARD_ME_1" value="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="0"/>
      <valve id="eN_WARD_VA_02" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d3_5a_3" fullName="3.5a Ueberstroemen von Dornum nach Drohne + Verdichten von Achim nach Drohne (3)">
      <compressorStation id="eN_WARD_ME_1" value="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="0"/>
      <valve id="eN_WARD_VA_02" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d3_5a_4" fullName="3.5a Ueberstroemen von Dornum nach Drohne + Verdichten von Achim nach Drohne (4)">
      <compressorStation id="eN_WARD_ME_1" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_2" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="0"/>
      <valve id="eN_WARD_VA_02" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_07" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="0"/>
      <valve id="eN_WARD_VA_12" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_13" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
    <decision id="d3_5b_2s" fullName="3.5b Ueberstroemen von Dornum nach Drohne + Verdichten von Achim nach Drohne (2 in Serie)">
      <compressorStation id="eN_WARD_ME_1" value="0"/>
      <compressorStation id="eN_WARD_ME_2" value="0"/>
      <compressorStation id="eN_WARD_ME_3" value="1" flowDirection="0"/>
      <compressorStation id="eN_WARD_ME_4" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_1" value="1" flowDirection="0"/>
      <controlValve id="eN_WARD_RS_2" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_01" value="0"/>
      <valve id="eN_WARD_VA_02" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_03" value="0"/>
      <valve id="eN_WARD_VA_04" value="0"/>
      <valve id="eN_WARD_VA_05" value="0"/>
      <valve id="eN_WARD_VA_06" value="0"/>
      <valve id="eN_WARD_VA_07" value="0"/>
      <valve id="eN_WARD_VA_08" value="0"/>
      <valve id="eN_WARD_VA_09" value="0"/>
      <valve id="eN_WARD_VA_10" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_11" value="1" flowDirection="0"/>
      <valve id="eN_WARD_VA_12" value="0"/>
      <valve id="eN_WARD_VA_13" value="0"/>
      <valve id="eN_WARD_VA_14" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_15" value="1" flowDirection="1"/>
      <valve id="eN_WARD_VA_17" value="0"/>
      <valve id="eN_WARD_VA_18" value="0"/>
      <valve id="eN_WARD_VA_19" value="0"/>
      <valve id="eN_WARD_VA_20" value="1" flowDirection="1"/>
    </decision>    
  </decisionGroup>
</combinedDecisions>
