# integer-sequence-search

This project is an offline integer sequence search engine. Lets call it ISS for short ;-)
ISS works on OEIS data files, so please take note of the OEIS license, see http://oeis.org/LICENSE.
ISS comes with its own set of transforms and is capable to discover sequence relations not documented in the OEIS yet.


## Releases

* v0.3 First revision with a meaningful README file.


## Getting Started

* Clone the repository, create a plain Java project importing it, make sure that 'src' is the source folder of your project, and add the jars from the lib-folder to your classpath. 

* You will need <strong>Java 9</strong> or higher for the project to compile.

* Download some OEIS data file (stripped.zip) from http://oeis.org/pages.html; unpack it and name the unpacked version stripped.txt.
Put that file into the main folder of your new Java project. Also, please take note of the OEIS license, see http://oeis.org/LICENSE.

* Now all you have to do is to run the main class de.tilman_neumann.iss.IntegerSequenceSearch, wait until the data file is loaded,
and then type in some OEIS A-number or your own comma-separated integer sequence.

* To analyze results, it is recommended to look into the log.txt file generated in the main folder of the project.
At the end of the file the best matches are listed, sorted by match sequence length. 
Searching backward in the logs for the A-numbers, you'll find the sequence transforms
and match matrices. If you are lucky, you'll be rewarded with one or more hypotheses on sequence relations
that have not been documented before.

There is hardly any documentation (check the /doc folder) and no support to ISS, 
so you should be ready to start exploring the source code.


## Remarks

* This program is far from finished, but it is stable and can already provide interesting information.
* Logging is still in a kind of debug mode (very verbose)
* Some sequences get many trivial hits; it would need some filters to improve the view on interesting matches.
* I'ld be happy about anybody contributing new transformations or documenting some transformations.


## Authors

* **Tilman Neumann** - *Initial work*


## License

This project is licensed under the GPL 3 License - see the [LICENSE](LICENSE) file for details


## Credits

The Online Encyclopedia of Integer Sequences (http://oeis.org).

