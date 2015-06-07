(: Wenlu Cheng (1336340)
   CSE 414 hw5
   05/11/15
:)

(: Problem 7. :)

<result>
  <waterbody>
    <name>Pacific Ocean</name>
    {
      for $x in doc("mondial.xml")/mondial,
          $sea in $x/sea[name/text() = "Pacific Ocean"]
      let $num := tokenize($sea/@country, '\s+')
      return 
        <adjacent_countries>
         {
           for $c in $num
            return 
                <country>
                  <name>{$x/country[@car_code = $c]/name/text() }</name>
                </country>
         }
         </adjacent_countries>
    }
  </waterbody>
</result>

(:Results
    <?xml version="1.0" encoding="UTF-8"?>
    <result>
      <waterbody>
        <name>Pacific Ocean</name>
        <adjacent_countries>
          <country>
            <name>Russia</name>
          </country>
          <country>
            <name>Japan</name>
          </country>
          <country>
            <name>Maldives</name>
          </country>
          <country>
            <name>Philippines</name>
          </country>
          <country>
            <name>Taiwan</name>
          </country>
          <country>
          .....
    </result>

:)