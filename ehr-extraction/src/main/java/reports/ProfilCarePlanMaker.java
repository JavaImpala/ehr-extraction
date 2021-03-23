package reports;

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
				.addListener(SimpleSequenceLineParser.listenOnce(EndableWrapper.wrap(l->{System.out.println("first "+l);})))
				.addListener(SimpleSequenceLineParser.listenOnce(EndableWrapper.wrap(l->{System.out.println("second "+l);})))
				.addListener(SimpleSequenceLineParser.listenOnce(EndableWrapper.wrap(l->{System.out.println("third "+l);})))
				.build();
	}
	
	public static ProfilCarePlanMaker create() {
		return new ProfilCarePlanMaker();
	}
	
	@Override
	public void readLine(String line) {
		System.out.println("planmaker leser linje "+line);
		
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
