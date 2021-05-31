package reports;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Base64BinaryType;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Reference;

import com.lowagie.text.pdf.codec.Base64;

import fhir.FHIRResources;
import reports.readers.FirstPageReportReader;
import reports.readers.NormalReportLineParser;
import reports.readers.ReportEndMatcher;
import util.DateParser;
import util.PageHeaderIdentifier;
import util.endable.EndableWrapper;
import util.matcher.ListenUntilMatchedOrUnmatched;
import util.pageProcessor.PageParser;
import util.pageProcessor.PageParserManager;
import util.sequence.EndableToSequenceLineParser;
import util.sequence.SequenceLineParsers;
import util.sequence.SimpleSequenceLineParser;

public class ProfilCarePlanPageParserManager implements PageParserManager{
	
	Supplier<PageParser> currentPageParserSupplier;
	
	private static int carePlanCounter=0;
	private static int careReportCounter=0;
	
	private ProfilCarePlanPageParserManager(FHIRResources client) {
		//lager parser
		
		carePlanCounter++;
		
		ProfilRawCarePlan carePlan=new ProfilRawCarePlan();
		
		ProfilCarePlanDescriptionMaker carePlanDescriptionMaker=ProfilCarePlanDescriptionMaker.create(m->{
			//carePlan.setDescriptions(m);
		});
		
		CarePlan fhirCarePlan=new CarePlan();
		
		//client.addResource(ref);
		List<DocumentReference> reports=new ArrayList<>();
		
		ProfilReportMaker reportMaker=ProfilReportMaker.create(
			report->{
				careReportCounter++;
				
				DocumentReference ref=new DocumentReference();
				
				
				ref.setId("DocumentReference/tromsokommune"+carePlanCounter+"-"+careReportCounter);
				
				ref.getContext().addRelated(new Reference(fhirCarePlan.getId()));
				
				ref.setSubject(new Reference(client.getPatient().getId().toString()));
				
				if(report.getAuthor()=="Ukjent") {
					return;
				}
				
				ref.addAuthor(new Reference(client.getPractitionerFactory().get(report.getAuthor()).getId()));
				ref.setDate(DateParser.parse(report.getStringDate()));
				
				ref.setDescription(fhirCarePlan.getTitle()+"-"+report.getDaytimeReportType());
				
				report.getDaytimeReportType();
				
				ref.addContent().setAttachment(
						new Attachment()
							.setTitle("content")
							.setContentType("text/plain")
							.setDataElement(new Base64BinaryType(Base64.encodeBytes(report.getContent().getBytes()))));
				
				ref.getContext().addRelated(new Reference(client.getCarePlanIdFactory().getId(carePlan)));
				
				//client.addResource(ref);
			});
		
		SequenceLineParsers reader=new SequenceLineParsers.Builder()
				.addListener(ListenUntilMatchedOrUnmatched.create(PageHeaderIdentifier.matcher.get()))
				.addListener(EndableToSequenceLineParser.wrap(ProfilCarePlanMaker.create((cp)->{
					carePlan.setDescriptors(cp);
				})))
				.addListener(
					SimpleSequenceLineParser.create(
							EndableWrapper.wrapWithOnEndedHook(
								l->{
									carePlanDescriptionMaker.readLine(l);
								},
								()->{
									//finalizing careplan when done on this section
									fhirCarePlan.setTitle(carePlan.getDescriptors().getAction());
									fhirCarePlan.setAuthor(new Reference(client.getPractitionerFactory().get(carePlan.getDescriptors().getAuthor()).getId()));
									fhirCarePlan.setId(client.getCarePlanIdFactory().getId(carePlan));
									fhirCarePlan.setSubject(new Reference(client.getPatient().getId().toString()));
									
									Period period=new Period();
									
									if(!carePlan.getDescriptors().getStartDate().equals("")) {
										period.setStart(DateParser.parse(carePlan.getDescriptors().getStartDate()));
									}
									
									if(!carePlan.getDescriptors().getEndDate().equals("")) {
										period.setEnd(DateParser.parse(carePlan.getDescriptors().getEndDate()));
									}
									
									fhirCarePlan.setPeriod(period);
									fhirCarePlan.addContributor(new Reference(client.getOrg().getId()));
									
									client.addResource(fhirCarePlan);
								}
							),
							()->Optional.empty(),
							()->Optional.of(ProfilCarePlanDescriptionMaker.endDescriptions.get())
						))
				.addListener(
					SimpleSequenceLineParser.create(
						EndableWrapper.wrapWithOnEndedHook(
							l->{
								reportMaker.readLine(l);
							},
							()->{
								//on ending reading reports
								
							}),
						()->Optional.empty(),
						()->Optional.empty()
					))
				.addListener(
					SimpleSequenceLineParser.create(
							EndableWrapper.wrapWithOnEndedHook(
								l->{
									
								},
								()->{
									//on ending reading receits
								}),
							()->Optional.of(ReportEndMatcher.endMatcher.get()),
							()->Optional.of(ReportEndMatcher.endReceitsMatcher.get()))
						)
				.build();
		
		/*
		 * .of(ReportEndMatcher.endMatcher.get()
		 */
		
		currentPageParserSupplier=()->{
			FirstPageReportReader firstParser= FirstPageReportReader.create(reader);
			
			currentPageParserSupplier=()->NormalReportLineParser.create(reader);
			
			return firstParser;
		};
		
		
	}
	
	public static ProfilCarePlanPageParserManager create(FHIRResources client) {
		return new ProfilCarePlanPageParserManager(client);
	}	

	@Override
	public PageParser getPageParser() {
		return currentPageParserSupplier.get();
	}
}
