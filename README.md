# integer-sequence-search

This project is an offline integer sequence search engine. Lets call it ISS for short ;-)
ISS works on OEIS data files, so please take note of the OEIS license, see http://oeis.org/LICENSE.
ISS comes with its own set of transforms and is capable to discover sequence relations not documented in the OEIS yet.


## Releases

* v0.3 First revision with a meaningful README file.


## Getting Started

* Clone the repository, create a plain Java project importing it, make sure that 'src' is the source folder of your project, and add the jars from the lib-folder to your classpath. 

* You will need Java 8 or higher for the project to compile.

* Download some OEIS data file (stripped.zip) from http://oeis.org/pages.html; unpack it and name the unpacked version stripped.txt.
Put that file into the main folder of your new Java project. Also, please take note of the OEIS license, see http://oeis.org/LICENSE.

* Now all you have to do is to start the main class de.tilman_neumann.iss.IntegerSequenceSearch and then type in some
OEIS A-number or your own comma-separated integer sequence. If you are lucky, you'll be rewarded with some sequence relations
that have not been documented before.

There is no further documentation or support to ISS, so you should be ready to start exploring the source code.


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

So far all my credits go to the great OEIS project (http://oeis.org).
Please respect its license, see http://oeis.org/LICENSE.
