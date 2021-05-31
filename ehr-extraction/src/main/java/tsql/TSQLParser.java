package tsql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TSQLParser {
public static void main(String[] args ){  
		
		String path="/media/tor003/kingston/sql/split/xaa";
		
		String output="/media/tor003/kingston/sql/split/xaa-conv.sql";
		
		File replFile=new File(output);
		
		
		
		try {
			
			FileWriter newFile=new FileWriter(output);
			
			BufferedReader lines=new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));
			
			String line="not set";
			
			int counter=0;
			
			while((line=lines.readLine())!=null) {
				
				String regexLine=line;
				
				for(RegexReplace r:replace) {
					regexLine=regexLine.replaceAll(r.getOldString(), r.getNewString());
				}
				
				newFile.write(regexLine+System.lineSeparator());
				
				System.out.println(regexLine);
				
				counter++;
				
			}
			
			lines.close();
			newFile.close();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
    }  
	
	private static List<RegexReplace> replace=new ArrayList<>();
	
	static {
		replace.add(new RegexReplace(";","GO"));

		replace.add(new RegexReplace("numeric","\\[numeric\\]"));

		replace.add(new RegexReplace("blob","\\[image\\]"));

		replace.add(new RegexReplace("datetime","\\[smalldatetime\\]"));

		replace.add(new RegexReplace("decimal","\\[decimal\\]"));

		replace.add(new RegexReplace("decimal(12,2)","\\[money\\]"));

		replace.add(new RegexReplace("decimal(12,2)","\\[smallmoney\\]"));

		replace.add(new RegexReplace("tinyint","\\[bit\\]"));

		replace.add(new RegexReplace("int","\\[smallint\\]"));

		replace.add(new RegexReplace("double","\\[float\\]"));

		replace.add(new RegexReplace("varchar","\\[char\\]"));

		replace.add(new RegexReplace("varchar","\\[varchar\\]"));

		replace.add(new RegexReplace("varchar","\\[nvarchar\\]"));

		replace.add(new RegexReplace("datetime","\\[datetime\\]"));

		replace.add(new RegexReplace("text","\\[text\\]"));

		replace.add(new RegexReplace("int","\\[int\\]"));

		replace.add(new RegexReplace("ON\\[PRIMARY\\]"));

		replace.add(new RegexReplace("TEXTIMAGE_ON \\[PRIMARY\\]"));

		replace.add(new RegexReplace("\\[dbo\\]\\."));

		replace.add(new RegexReplace("ALTER TABLE [^;]* DEFAULT [^;]*."));

		replace.add(new RegexReplace("`","\\["));

		replace.add(new RegexReplace("`","\\]"));

		replace.add(new RegexReplace("COLLATE [^ ]+."));

		replace.add(new RegexReplace("AUTO_INCREMENT","IDENTITY\\([^)]+.v"));

		replace.add(new RegexReplace("AUTO_INCREMENT","IDENTITY \\([^)]+."));

		replace.add(new RegexReplace("WITH \\([^)]+."));

		replace.add(new RegexReplace("WITH\\([^)]+."));

		replace.add(new RegexReplace("ALTER TABLE [^;]* NOCHECK [^;]*."));

		replace.add(new RegexReplace("NOT FOR REPLICATION"));

		replace.add(new RegexReplace("CLUSTERED"));

		replace.add(new RegexReplace("SET ANSI_NULLS [^;]*."));

		replace.add(new RegexReplace("SET QUOTED_IDENTIFIER [^;]*."));

		replace.add(new RegexReplace("SET ANSI_PADDING [^;]*.v"));
	}
	

	private static class RegexReplace{
		private final String oldString;
		private final String newString;
		
		public RegexReplace(String newString, String oldString) {
			
			this.oldString = oldString;
			this.newString = newString;
		}
		
		public RegexReplace(String oldString) {
			
			this.oldString = oldString;
			this.newString = "";
		}
		
		private RegexReplace create(String oldString, String newString,boolean rep) {
			return null;
		}

		public String getOldString() {
			return oldString;
		}

		public String getNewString() {
			return newString;
		}
	}
}
