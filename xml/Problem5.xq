(: Wenlu Cheng (1336340)
   CSE 414 hw5
   05/11/15
:)

(: Problem 5. :)

<result>
	{
		for $x in doc("mondial.xml")/mondial,
			$t in $x/country
		where count($x/mountain[located/@country = $t/@car_code][height > 2000]) >= 3
		return
		<country>
			<name> {$t/name/text()}</name>
			{
				for $a in $x/mountain[located/@country=$t/@car_code]
				return <mountain>
							<name>{ $a/name/text()}</name>
							<height>{ $a/height/text()}</height>
						</mountain>
			}
		</country>
	}
</result>

(:Result -- I returned first 4 countries in the result since the spec asked us to show the first 3 returned results. 
	<?xml version="1.0" encoding="UTF-8"?>
	<result>
	  <country>
	    <name>France</name>
	    <mountain>
	      <name>Mont Blanc</name>
	      <height>4808</height>
	    </mountain>
	    <mountain>
	      <name>Barre des Ecrins</name>
	      <height>4101</height>
	    </mountain>
	    <mountain>
	      <name>Grand Ballon</name>
	      <height>1424</height>
	    </mountain>
	    <mountain>
	      <name>Puy De Dome</name>
	      <height>1465</height>
	    </mountain>
	    <mountain>
	      <name>Puy de Sancy</name>
	      <height>1885</height>
	    </mountain>
	    <mountain>
	      <name>Vignemale</name>
	      <height>3298</height>
	    </mountain>
	    <mountain>
	      <name>Monte Cinto</name>
	      <height>2706</height>
	    </mountain>
	  </country>
	  <country>
	    <name>Spain</name>
	    <mountain>
	      <name>Vignemale</name>
	      <height>3298</height>
	    </mountain>
	    <mountain>
	      <name>Pico de Aneto</name>
	      <height>3404</height>
	    </mountain>
	    <mountain>
	      <name>Torre de Cerredo</name>
	      <height>2648</height>
	    </mountain>
	    <mountain>
	      <name>Pico de Almanzor</name>
	      <height>2648</height>
	    </mountain>
	    <mountain>
	      <name>Moncayo</name>
	      <height>2313</height>
	    </mountain>
	    <mountain>
	      <name>Mulhacen</name>
	      <height>3482</height>
	    </mountain>
	    <mountain>
	      <name>Pico de Teide</name>
	      <height>3718</height>
	    </mountain>
	    <mountain>
	      <name>Pico de los Nieves</name>
	      <height>1949</height>
	    </mountain>
	    <mountain>
	      <name>Roque de los Muchachos</name>
	      <height>2426</height>
	    </mountain>
	  </country>
	  <country>
	    <name>Austria</name>
	    <mountain>
	      <name>Zugspitze</name>
	      <height>2963</height>
	    </mountain>
	    <mountain>
	      <name>Grossglockner</name>
	      <height>3797</height>
	    </mountain>
	    <mountain>
	      <name>Hochgolling</name>
	      <height>2862</height>
	    </mountain>
	  </country>
	  <country>
	    <name>Italy</name>
	    <mountain>
	      <name>Mont Blanc</name>
	      <height>4808</height>
	    </mountain>
	    <mountain>
	      <name>Matterhorn</name>
	      <height>4478</height>
	    </mountain>
	    <mountain>
	      <name>Monte Rosa</name>
	      <height>4634</height>
	    </mountain>
	    <mountain>
	      <name>GranParadiso</name>
	      <height>4061</height>
	    </mountain>
	    <mountain>
	      <name>Piz Bernina</name>
	      <height>4048</height>
	    </mountain>
	    <mountain>
	      <name>Marmolata</name>
	      <height>3343</height>
	    </mountain>
	    <mountain>
	      <name>Gran Sasso</name>
	      <height>2912</height>
	    </mountain>
	    <mountain>
	      <name>Monte Falterona</name>
	      <height>1654</height>
	    </mountain>
	    <mountain>
	      <name>Vesuv</name>
	      <height>1281</height>
	    </mountain>
	    <mountain>
	      <name>Etna</name>
	      <height>3323</height>
	    </mountain>
	  </country>
		....
	</result>  
:)