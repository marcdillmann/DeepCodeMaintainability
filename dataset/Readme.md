# Readme

This archive was created along the work described in detail in

*M. Schnappinger, A. Fietzke, and A. Pretschner, "Defining a Software Maintainability Dataset: Collecting, Aggregating and Analysing Expert Evaluations of Software Maintainability", International Conference on Software Maintenance and Evolution (ICSME), 2020*

If you use this dataset in your  reasearch, please cite both the dataset and the corresponding paper:

```
@misc{schnappinger2020Dataset,
    title={A Software Maintainability Dataset},
    url={https://figshare.com/articles/dataset/_/12801215/0},
    DOI={10.6084/m9.figshare.12801215},
    publisher={figshare},
    author={Schnappinger, Markus and Fietzke, Arnaud and Pretschner, Alexander},
    year={2020},
    month={Sep},
}

@inproceedings{schnappinger2020Defining,
	title={Defining a Software Maintainability Dataset: Collecting, Aggregating and Analysing Expert Evaluations of Software Maintainability},
	author={Schnappinger, M. and Fietzke, A. and Pretschner, A.},
	booktitle={ICSME 2020. IEEE International Conference on Software Maintenance and Evolution},
	year={2020},
	organization={IEEE}
}
```





# Content

This publicly available dataset contains evaluations on class-level software maintainability. This dataset can be used to develop and evaluate automated quality prediction tools.

In [1], we present a large scale study that created this dataset based on expert evaluations. In total, 70 software analysts, researchers, and developers working with e.g. Facebook, Oracle, or BMW contributed around 2,000 manual assessments. 

The archive contains

- This readme with all relevant information about
  - Study objects
  - Label definition
  - Threats to validity
  - Hints for using the dataset
  - List of metrics used to prioritize the samples
- The code of the study objects
- a csv containing the readability, understandability, complexity, adequate size, and overall maintainability labels



## Study Objects

We selected the following projects as sources for the code snippets:

- ArgoUML: Tool to design, simulate and generate code from UML diagrams [2] 
- Art of Illusion: 3D Modeling and Rendering Studio [3]
- Diary Management: Multi-user calendar tool [4]
- JUnit 4: Testing framework for Java programs [5]
- JSweet: Transpiler to convert Java code into Javascript or Typescript code [6]

Table I in [1] describes the release date of the systems, their size, and the number of files for each project in detail. What sticks out in this table in the varying release year. Since Java is a rapidly evolving programming language, we wanted to include code that does use newer features of Java as well as older code that sticks to basic features. Furthermore, we deliberately did not use the latest release for every project, e.g. ArgoUML. This enables future research to compare the maintainability, i.e. the maintenance effort predicted by the experts, to the actually required effort. 

The commercial systems mentioned in [1] are not part of this dataset.



## Label Definition

This study focuses on maintainability. In the context of this work, we understand maintainability as the estimated future maintenance effort of a program class. This effort is influenced by several factors.

We ask the participants to rate each code snippet in five dimensions: readability, understandability, complexity, modularity, and overall maintainability. For every dimension, we post one statement and the analysts express their opinion on a four-part Likert-scale:

- This code is easy to read
- The semantic meaning of this code is clear
- This code is complex
- This code should be split up into smaller pieces
- Overall, this code is maintainable 



In our study, every code snippet was assessed by at least three experts. Eventually, the Expectation-Maximization utilizes the experts' individual error rates to compute probabilities for each class to be the true label. The labels reported here therefore vector of the form `[ P(Class0), P(Class1), P(Class2), P(Class3)]`.  `P(Class0)` denotes the probability that the first answer on the Likert-scale is correct, i.e. the code is easy to read, easy to understand, etc.  Please note, the questionnaire uses negative statements to assess complexity and modularity of the code. 

You can either interpret the class with the highest probability as the final label or use a weighted average. 



Please respect the threats to validity mentioned below.



## Threats to Validity and Hints for Usage

The labeling platform presents code snippets to experts who evaluate the code. The granularity of Java classes was chosen to keep the labeling effort feasible. This implies that only intra-class characteristics can be evaluated. We are aware that major aspects of software quality are inter-class attributes such as cloning and coupling. These are not characteristics of one isolated code snippet, but a class and its wider context. We limited the scope of the assessments to intraclass characteristics to foster the practicality and scalability
of the labeling. Had we included the context of a class in the assessment, the labeling would have been far too time consuming and complex to collect a reasonable amount of labels. An interesting continuation of this work would be to include more context and inter-class relationships. 

One of the most interesting characteristics of the created maintainability dataset is its unbalanced distribution. The majority of the files were evaluated to be easily maintainable, while only a few files were actually negatively perceived. Table III in [1] shows the more fine-grained description. However, we decided not to trim the dataset to a balanced form. We took great care to include negative examples while keeping in representative.

The study projects are to large to label all their files manually. To make efficient use of the experts’ time, the snippets are labeled in a specific order. This prioritization is based on static code metrics. A list of the used metrics in attached below.

To mitigate the effect of biased evaluators, we collaborated with 70 participants from 17 different companies. To avoid domain or project-specific bias, we included code from 9 projects, both open and closed source. Still, the analysed code is only written in Java. Limiting the study to one language eliminates the need to frequently accustom to new contexts. We chose Java because of its high relevance in industry.

 The granularity of Java classes was chosen to keep the labeling effort feasible. This implies that
only intra-class characteristics can be evaluated. We are aware that major aspects of software quality are inter-class attributes such as cloning and coupling. These are not characteristics of one isolated code snippet, but a class and its wider context. We limited the scope of the assessments to intraclass characteristics to foster the practicality and scalability of the labeling. Had we included the context of a class in the assessment, the labeling would have been far too time consuming and complex to collect a reasonable amount of labels. An interesting continuation of this work would be to
include more context and inter-class relationships. 





## Static Code Metrics used for the Prioritization of Samples

// Please see the references and documention of the tools to learn more about the single metrics.

**From Conqat [7]:**
loc, sloc, standardsloc, max loop depth, max loop length, sloc_depth_greater_4, conditions, asserts, loopcount, code in comments, sloc_depth_greater_4_percent, condition ratio, method loc, method sloc, non-methods sloc, code type exception sloc, code type other sloc, max methods loc, max methods sloc, avg methods loc, avg methods sloc, coneunits_50nn, unitcoverage_50nn, rfsloc_50nn, cloneunits_150no, unitcoverge_150no, rfsloc_150no

**From Teamscale [8]:** 
longest method length, method length findings red, method length findings yellow, method length findings green, maximum nesting depth, nesting depth findings red, nesting depth findings yellow, nesting depth findings green, number of findings, number of findings red, number of findings yellow, number of findings green, findings density, findings density yellow, findings density red, clone coverage, comment completeness assessment red, comment completeness assessment green

**From SonarQube [9]:**
number of bugs, code smells, cognitive complexity, complexity, duplicated blocks, duplicated lines, effort to reach maintainability rating A, functioncount, reliability rating, reliability remediation effort, security rating, security remediation effort, statements, squale index, squale debt ratio

**From Designite [10]:** 
nof, nopf, nom, nopm, fanin, fanout

**Cohesion Metrics** from own implementations: LCOM, LCOM2, LCOM3, LCOM4, LCOM5, TLCOM, Co prime, lcomco3, lcomco4, lcomco prime, coh, scom, cc, tcc, lcc, dcd, dci, occ, pcc, camc, nhd, snhd, cbo cohesion, rfc, rfc prime, mpc, dac, dac prime

**From SD Metrics [11]:**
 cld, numasses_ssc, numassel_nsb, ec_attr, ic_attr, ec_par, ic_par

**Network metrics** from own implementation:
eccentricity, closeness centrality, betweenesscentrality, pagerank eigencentrality

**From Sourcemeter [12]**:
ccl, cco, ci. clc, cllc, lldc, nle, cbo, cboi, nii, noi, ad, cd, cloc, dloc, tcd, tcloc, dit, noa, noc, nod, nop, ng, nla, nlg, nlm, nlpa, nlpm, nls, warning info, warning major, warning minor, basic rules, brace rules, clone implemtantaion rules, clone metric rules, cohesion metric rules, complexity metric rules, controversial rules, coupling metric rules, design rules, documentation metric rules, empty code rules, import statment rules, inheritance metric rules, j2ee rules, junit rules, jakarta commonsloggingsrules, java logging rules, naming rules, optimization rules, security code guideline rules, size metric rules, strict exception rules, string and string buffer rules, typeresolution rules, unnecessary and unused code rules.



## References

[1] M. Schnappinger, A. Fietzke, and A. Pretschner, "Defining a Software Maintainability Dataset: Collecting, Aggregating and Analysing Expert Evaluations of Software Maintainability", International Conference on Software Maintenance and Evolution (ICSME), 2020
[2] http://argouml.tigris.org/
[3] http://www.artofillusion.org/
[4] https://sourceforge.net/projects/diarymanagement/
[5] https://junit.org/junit4/
[6] https://www.jsweet.org/

[7] Deissenboeck, Florian, et al. "Tool support for continuous quality control." *IEEE software* 25.5 (2008): 60-67.

[8] Heinemann, Lars, Benjamin Hummel, and Daniela Steidl. "Teamscale: Software quality control in real-time." *Companion Proceedings of the 36th International Conference on Software Engineering*. 2014.

[9] Campbell, G., and Patroklos P. Papapetrou. *SonarQube in action*. Manning Publications Co., 2013.

[10] Sharma, Tushar, Pratibha Mishra, and Rohit Tiwari. "Designite: A software design quality assessment tool." *Proceedings of the 1st International Workshop on Bringing Architectural Design Thinking into Developers' Daily Activities*. 2016.

[11] Wust, J. "SDMetrics: The software design metrics tool for UML." (2005).

[12] https://www.sourcemeter.com/

