package reports;

import java.util.Iterator;

import util.RegexTools;
import util.endable.EndableLineParser;
import util.endable.EndableWrapper;
import util.sequence.SequenceLineParsers;
import util.sequence.SimpleSequenceLineParser;

public class ProfilCarePlanMaker implements EndableLineParser{

	private final CarePlan plan;

	private final SequenceLineParsers parser;
	
	
	
	private ProfilCarePlanMaker() {
		plan=new CarePlan();
		
		this.parser=new SequenceLineParsers.Builder()
				.addListener(SimpleSequenceLineParser.listenOnce(EndableWrapper.wrap(l->{
					//Plan/rapport
					
				})))	
				.addListener(SimpleSequenceLineParser.listenOnce(EndableWrapper.wrap(l->{
					//Plankategori: Helsehjelp
					
					plan.setPlanCategory(RegexTools.getLastWordOfString(l));
				})))	
				.addListener(SimpleSequenceLineParser.listenOnce(EndableWrapper.wrap(l->{
					//Planområde: Kontakt lege/nettverk/pårørend
					plan.setPlanArea(RegexTools.getLastWordOfString(l));
				}))) 	
				.addListener(SimpleSequenceLineParser.listenOnce(EndableWrapper.wrap(l->{
					//Tiltak: Pasientsentrert team 27.04.2018 - 06.11.2019 Skrevet av: Torbjørn Torsvik
					
					
					plan.setAction(RegexTools.getValueAfterAndBefore(l,"Tiltak:\\s","\\s(((3[01]|[12][0-9]|0[1-9]).(1[0-2]|0[1-9]).[0-9]{4}))"));
					plan.setAuthor(RegexTools.getValueAfter(l,"Skrevet av:"));
					
					Iterator<String> dates=RegexTools.getMatches(l,"[0-9]{2}[.][0-9]{2}[.][0-9]{4}").iterator();
					
					if(dates.hasNext()) {
						plan.setStartDate(dates.next());
					}
					
					if(dates.hasNext()) {
						plan.setEndDate(dates.next());
					}
					
				}))) 	
				
				.build();
	}
	
	public static ProfilCarePlanMaker create() {
		return new ProfilCarePlanMaker();
	}
	

	
	@Override
	public void readLine(String line) {
		
		this.parser.readLine(line);
		
		if(this.parser.isEnded()) {
			System.out.println("finished!");
		}
	}

	@Override
	public boolean isEnded() {
		return parser.isEnded();
	}

	@Override
	public void end() {
		parser.end();
	}
}