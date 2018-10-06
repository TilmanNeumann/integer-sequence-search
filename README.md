# integer-sequence-search

This project is an offline integer sequence search engine. Lets call it ISS for short ;-)
ISS works on OEIS data files and is capable to discover sequence relations not documented in the OEIS yet.
All you have to do is to type in a comma-separated number sequence or an OEIS A-number.
Then see...


## Releases

* v0.3 First revision with README file.


## Getting Started

Clone the repository, create a plain Java project importing it, make sure that 'src' is the source folder of your project, and add the jars from the lib-folder to your classpath. 

You will need Java 8 or higher for the project to compile.

This project comes with a sample OEIS data file (data/stripped.txt). You may replace it with the latest data file from http://oeis.org/pages.html. Please take note of the OEIS license, see http://oeis.org/LICENSE.

There is no documentation and no support, so you should be ready to start exploring the source code.
The main class where you start a search is class IntegerSequenceSearch.


## Remarks

* This program is far from finished, but it is stable and can already provide interesting information.
* Logging is still in a kind of debug mode (very verbose)
* Some sequences get many stupid hits; it would need some filters to improve the view on interesting matches.


## Authors

* **Tilman Neumann** - *Initial work*


## License

This project is licensed under the GPL 3 License - see the [LICENSE](LICENSE) file for details


## Credits

So far all my credits go to the great OEIS project (http://oeis.org).
Please respect its license, see http://oeis.org/LICENSE.
