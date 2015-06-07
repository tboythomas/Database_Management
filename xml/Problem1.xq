(: Wenlu Cheng (1336340)
   CSE 414 hw5
   05/11/15
:)

(: Problem 1. :)

<result>
	<country>
		<name>Peru</name>
  { 
      for $x in doc("mondial.xml")/mondial/country[name='Peru']//city/name/text()
      order by $x ascending
      return <city><name> { $x } </name></city>

  }
  	</country>
</result>

(:Results
<?xml version="1.0" encoding="UTF-8"?>
      <result>
        <country>
          <name>Peru</name>
          <city>
            <name>Abancay</name>
          </city>
          <city>
            <name>Arequipa</name>
          </city>
          <city>
            <name>Ayacucho</name>
          </city>
          <city>
            <name>Cajamarca</name>
          </city>
          <city>
            <name>Callao</name>
          </city>
          ...
      </result>
:)

