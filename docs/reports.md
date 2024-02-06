# FloreantPOS

Quick Guide to Editing Reports and Receipt Formats

### vim:

To manually edit the reports and receipts, you can edit the .jrxml files in src/com/floreantpos/report/template by hand.  Then place the edited files in config/printerlayouts in the output directory and remove any compiled .jasper files present.  At runtime use of the report, the .jrxml file should be compiled into a .jasper file and used for the report.

### iReport-Designer:

To get the iReport-Designer compatible with floreant, you should use 5.6.0 or later.  iReport-Designer may require Java 1.7 sdk.
JasperSoft Studio 6.21.0 appears to work for our limited uses with the 5.6.0 library, but requires an online login.

iReport-Designer (iRD)for JasperReports Files: iReport-5.6.0
https://sourceforge.net/projects/ireport/files/iReport/iReport-5.6.0/

Java SE Development Kit 7u80
https://www.oracle.com/java/technologies/javase/javase7-archive-downloads.html

Download both and extract to two directories.

cd iReport-5.6.0/bin
./ireport --jdkhome <your jdk dir here>

Open the .jrxml file within iRD.  Edit until content.  Compile the file to .jasper form, it will be saved to the current directory.  If you update only the .jrxml file within the templates directory, the changes will not be reflected in floreant, you must also save the .jasper file.
	
To try out your new reports run "mvn clean" or delete the contents of two directories and build again.
	
rm target/floreantpos-bin/floreantpos/config/printerlayouts/*

rm target/classes/com/floreantpos/report/template/*

mvn package
