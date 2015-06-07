(: Wenlu Cheng (1336340)
   CSE 414 hw5
   05/11/15
:)

(: Problem 2. :)

<result>
  { 
      for $x in doc("mondial.xml")/mondial/country
      let $num := count(distinct-values($x/province/@id))
      where $num > 20
      order by $num descending
      return <country num_provinces = "{$num}"> 
      		 	<name>{ $x/name/text() }</name>
      		 </country>
  }
</result>

(: Results
		<?xml version="1.0" encoding="UTF-8"?>
		<result>
		  <country num_provinces="81">
		    <name>United Kingdom</name>
		  </country>
		  <country num_provinces="80">
		    <name>Russia</name>
		  </country>
		  <country num_provinces="73">
		    <name>Turkey</name>
		  </country>
		  <country num_provinces="51">
		    <name>United States</name>
		  </country>
		  ......
		 </result>
:)
