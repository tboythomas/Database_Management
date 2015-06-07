(: Wenlu Cheng (1336340)
   CSE 414 hw5
   05/11/15
:)

(: Problem 3. :)

<result>
  <country>
  <name>United States</name>
    {
        for $x in doc("mondial.xml")/mondial/country[name='United States']/province
        let $ans := number($x/population) div number($x/area)
        order by $ans descending
        return
              <state>
                      <name>{$x/name/text()}</name>
                      <population_density>{$ans}</population_density>
              </state>
    }
  </country>
</result>



(: 
  Results
          <?xml version="1.0" encoding="UTF-8"?>
          <result>
            <country>
              <name>United States</name>
              <state>
                <name>Distr. Columbia</name>
                <population_density>2955.106145251397</population_density>
              </state>
              <state>
                <name>New Jersey</name>
                <population_density>399.28842721142405</population_density>
              </state>
              <state>
                <name>Rhode Island</name>
                <population_density>314.56801529149413</population_density>
              </state>
              <state>
                <name>Massachusetts</name>
                <population_density>285.13260312281517</population_density>
              </state>
              <state>
                <name>Connecticut</name>
                <population_density>251.58559667615603</population_density>
              </state>
              ...
            </country>
          <result>
: )