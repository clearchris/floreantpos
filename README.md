# FloreantPOS

**Update 7-3-22**:  Version bumps to postgresql, mysql-connector-java, & derby (security from @dependabot).  Also some changes to make build complete.

**Update 2-28-22**: Updated xercesImpl from 2.12.0 to 2.12.2

**Update 1-3-22**:  Updated log4j-core to 2.17.1 due to log4shell vulnerability

**Update 3-11-21**:  Quick note:  Apparently FloreantPOS's LICENSE's web link to http://floreantpos.org/license.html/ has become broken ("Not Found The requested URL was not found on this server.").  A 10/18/19 archive of that license (with attribution clauses in Exhibits) is [currently available](http://web.archive.org/web/20191018191240/http://floreantpos.org:80/license.html/).

**Update 2-12-20**:  Github had another warning related to CVE-2019-17571 and log4j, so a more updated, hopefully patched version was included in pom.xml.  This may break the build.  Not sure.

**Update 1-23-19**:  I did another quick update.  Looks like the source on the /trunk branch hasn't been updated since 8/20/18.  Also, github had a security alert
for pom.xml (org.apache.httpcomponents was very old) so i patched it.  no idea if it will break the build.  Enjoy.

**Update 2-16-18**:  Even though this repo was set up a few years ago, I've noticed that people are still forking it.  So I figured I'd do an update to the repo.  The official SVN repository has file system errors in revision [1215](https://sourceforge.net/p/floreantpos/code/1215), [1216](https://sourceforge.net/p/floreantpos/code/1216), and [1217](https://sourceforge.net/p/floreantpos/code/1216),
 so the revisions were included in 1218.  Also note that in the original repository there are several branches.  This is the "TRUNK".

A free, open-source point-of-sale application for restaurants (and potentially more)

Licensed under the [MRPL](http://www.floreantpos.org/license.html) (a modified MPL), FloreantPOS is a
java-based multi-terminal "point of sale" system intended for restaurants.  It uses an [Apache derby](https://db.apache.org/derby/),
[mysql/mariadb](https://mariadb.org/), or [postgresql](https://postgresql.org) back end database.  Your choice.

FloreantPOS has also been (experimentally) known to run as a web app in a browser when paired with [webswing](http://webswing.org/#!/home).  I posted a somewhat
old set of instructions for trying Floreant as a web app [here](https://fat-tire.github.io/floreantpos.html) and I have posted the source code for an Android app to access this web app.  It's called <a href="https://www.github.com/fat-tire/hippos">HipPOS</a>.

Much of FloreantPOS's development code is currently hosted at [sourceforge.net](http://sourceforge.net/projects/floreantpos/) using [subversion](https://subversion.apache.org/), but I am hoping that the lead developers come to their senses and switch to git soon.  But until they do, this github repository
is just a mirror and may not contain the very latest updates.

If you'd like to automatically build FloreantPOS from the original svn code, I have written a [shell script](https://en.wikipedia.org/wiki/Bash_(Unix_shell)) to do so (in Ubuntu Linux), which
I've made available under the GPL called [floreantpos_updater](https://github.com/fat-tire/floreantpos_updater).  This can automatically install build dependencies, update the source code from Sourceforge, and build into a .zip as well as an "active" directory which could be used to run floreant.

Or, if you'd like to build with eclipse or netbean, you can find instructions [here](http://floreant.org/support/development/#tab-1436629735676-3-1) on the Floreant Web site.

Floreant's main web site is [here](http://floreant.org)
Their main forum is [here](http://forum.floreantpos.org/forumdisplay.php?2-Main-Forum).

Floreant's lead developers are at [OROCUBE LLC](https://orocube.com/), and they offer additional plugins for Floreant, which I believe are currently (11/21/15) a free download, though closed-source.

Finally-- with respect to branches:

* `master` -- this is the original code from the sourceforge svn repository.  It may not be fully up-to-date as it must be manually synced with upstream's svn repository.
* `fattire-master` -- This contains my additions, such as the LICENSE file as well as this README.md and anything else I may feel like adding.  See the commit log for details.
* `2018-fattire-master` -- an update to the previous as of 2/16/18.
* `2019-fattire-master` -- an update to the previous as of 1/23/19 + a few security patches
