# GASSER

[![Watch the video](https://img.youtube.com/vi/20Uf1ugEvAQ/maxresdefault.jpg)](https://youtu.be/20Uf1ugEvAQ)

<div align="center">
  A screencast of GASSER in action.
</div>


**GASSER** (Genetic Algorithm fot teSt SuitEReduction) implements a **Genetic Algorithm** (GA) Based approach for
Test Suite Reduction and a number of instances of the process underlying this approach. The goal of GASSER
is to reduce the original Test Suites by identify reduced Test Suites which maximize both statement coverage
and test-case-statement-coverage diversity, while minimize the number of test cases. To pursue such a threefold
objective GASSER, starting from initial randomly created reduced Test Suites, improves them by the use
of GA (namely **NSGAII**). 
In particular, at each iteration a fitness function is computed for each Test Suite based
on three objectives: 

* statement coverage;
* test-case-statement-coverage diversity (or simply test-case
diversity);
* test suite size. 

To estimate the test-case diversity, GASSER considers a number of measure
(e.g., Hamming Distance), each of them represents a GASSER instance. Then the fittest reduced Test Suites are
selected to define new better reduced Test Suites by the use of genetic operators (i.e., mutation and crossover).

### Environment

* GASSER is a Java console program which works on each machine on which a Java 8 is installed. You can
download it from https://www.java.com/it/download/. 

* To use the String Kernels-Based Dissimilarity you must install R from https://cran.r-project.org/bin/windows/base/old/3.3.1/ and the kernlab and rjava packages. Packages
can be installed with the install.packages(‘package_name’) function in R.

### Running GASSER
* To run [GASSER.jar](GASSER-jar/) you have to open a Console in the GASSER folder and type: 

`java –jar it.unibas.baselab.gasser_xxxx.jar`

* Now follow the instructions provided by the tool that will allow you to complete the GASSER
configuration (e.g., coverage file path, dissimilarity measure, maxEvaluations value and
maxPopulationSize value). 

In particular:
  1. Dissimilarity file path: If you have a file with the test cases dissimilarity matrix, GASSER will
  ask you for its complete path (.csv file);
  2. Dissimilarity measure: if you do not have the test cases dissimilarity matrix, GASSER will ask
  you which one (among all of those possible) you want to use (see Figure 2). GASSER will
  compute this information on the fly during its execution;
  3. Coverage file path: the complete path of the test cases statement coverage XML file;
  4. SUT name: The name of your System Under Test;
  5. maxPopulationSize and maxEvaluations: The possibility to change the value for
  maxPopulationSize (100 is the default value) and maxEvaluations (1000 is the default value).
  
* Wait the GASSER execution end and enjoy your result files: **POPULATION_SUT name.tsv** and
**COMPLETE_SOLUTION_SUT name.tsv**
