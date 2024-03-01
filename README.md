# FloreantPOS

**Java Retail and Restaurant Point of Sale System**

Forked from https://github.com/fat-tire/floreantpos because of the svn corruption fat-tire mentions in his 2-16-18 update.  Fat-tire has also made a [swing web app](https://fat-tire.github.io/floreantpos.html) for floreant, and also an [android app](https://www.github.com/fat-tire/hippos) to access the web app.  Finally, he has also written some [deployment scripts](https://github.com/fat-tire/floreantpos_updater).

Original authors can be found at [Orocube](http://floreant.org)

**Branches:**
* 'bringup15' -- Code forked from 2019-fattire-master at [6ef029b](https://github.com/clearchris/floreant/commit/6ef029bbfe3d7513e80c90c1de931f7b7b816444). Updates then applied via cherry picks from orocube svn to bring the code base up to v1.5.  Then remaining relevant commits cherry picked from 2019-fattire-master.
* 'bringupfeatures' -- Current work branch.  Contains all commits from bringup15, has many new features, and is not compatible with mainline floreant.

**Notable changes on the current branch include:**
* Native support for selling and redeeming gift certificates including tracking remaining value 
* High resolution monitor support
* Enhanced visibility for lower vision users, including adding colors, and glass pane effects
* Rework of discount system
* Improvement to reports and receipts.  Receipts are currently made for an 80mm receipt printer.
* Many fixes and other improvements ongoing

**General Notes:**

I used floreant for years at a business, and am currently running it at another.  There does not seem to be a path for contributing patches upstream (to orocube), and if there were, I'd happily do so.

Pull requests welcome.

There are a number of old floreantpos forks on github, which may have interesting code and features.  I have had more success using external search engines to seach for floreantpos forks on github than by using github's search tools.

**Suggested hardware:**
* 1080p touchscreen
* 80mm receipt printer

**Community:**

Orocube does not have a public community since the forum was shut years ago.

Unofficial FloreantPOS Discord Server: https://discord.gg/xgSva52yBQ

**Going forward:**
* More reporting

**Development Documentation:**
* [Instructions on changing receipts or reports](/docs/reports.md)