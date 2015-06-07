(: Wenlu Cheng (1336340)
   CSE 414 hw5
   05/11/15
:)

(: Problem 4. :)


<result>
	<country>
		<name>United States</name>
			{
				for $x in doc("mondial.xml")/mondial/country[name = 'United States'],
				$s in $x/province[population > 11000000]
				let $r := number($s/population) div number($x/population)
				order by $r descending
				return
					<state>
						<name>{$s/name/text()}</name>
						<population_ratio>{$r}</population_ratio>
					</state>
			}
	</country>
</result>


(: Results
	<?xml version="1.0" encoding="UTF-8"?>
	<result>
	  <country>
	    <name>United States</name>
	    <state>
	      <name>California</name>
	      <population_ratio>0.12109258370833294</population_ratio>
	    </state>
	    <state>
	      <name>Texas</name>
	      <population_ratio>0.07294959666165857</population_ratio>
	    </state>
	    <state>
	      <name>New York</name>
	      <population_ratio>0.06806319172620687</population_ratio>
	    </state>
	    <state>
	    ....
	   </country>
	</result>
:)