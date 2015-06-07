(: Wenlu Cheng (1336340)
   CSE 414 hw5
   05/11/15
:)

(: Problem 6. :)

<html>
	<head>
		<title>Problem 6</title>
	</head>
	<body>
		<h1>Problem 6</h1>
		<ul>
		  {
		    for $x in doc("mondial.xml")/mondial,
				$t in $x/river
			let $count := tokenize($t/@country, '\s+')
			let $num := count($count)
			where $num >= 2
			order by $num descending
			return 
			<li>
			     <div>{ $t/name/text() }</div>
				 <ol>
				 {
				   for $c in $count
				   return <li>{ $x/country[@car_code = $c]/name/text() }</li>
				 }
				 </ol>
			</li>
		  }
		</ul>
	</body>
</html>

(: Results -- the elements in output file
	<?xml version="1.0" encoding="UTF-8"?>
	<html>
	  <head>
	    <title>Problem 6</title>
	  </head>
	  <body>
	    <h1>Problem 6</h1>
	    <ul>
	      <li>
	        <div>Donau</div>
	        <ol>
	          <li>Serbia</li>
	          <li>Austria</li>
	          <li>Germany</li>
	          <li>Hungary</li>
	          <li>Croatia</li>
	          <li>Slovakia</li>
	          <li>Bulgaria</li>
	          <li>Romania</li>
	          <li>Moldova</li>
	          <li>Ukraine</li>
	        </ol>
	      </li>
	      <li>
	        <div>Rhein</div>
	        <ol>
	          <li>Germany</li>
	          <li>Switzerland</li>
	          <li>Liechtenstein</li>
	          <li>Austria</li>
	          <li>France</li>
	          <li>Netherlands</li>
	        </ol>
	      </li>
	      <li>
	        <div>Drau</div>
	        <ol>
	          <li>Italy</li>
	          <li>Austria</li>
	          <li>Slovenia</li>
	          <li>Croatia</li>
	          <li>Hungary</li>
	        </ol>
	      </li>
	      ...
	</html>
:)