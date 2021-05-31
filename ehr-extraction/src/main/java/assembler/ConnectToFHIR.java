package assembler;

import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Base64BinaryType;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Reference;

import com.lowagie.text.pdf.codec.Base64;

public class ConnectToFHIR {
	
	public static void main(String[] args) {
    	
		//FhirContext ctx=FhirContext.forR4();
		
		
		//String serverBase = "http://fhirtest.uhn.ca/baseDstu2";
		//String serverBase = "http://localhost:8080/fhir";
		
		//IGenericClient client = ctx.newRestfulGenericClient(serverBase);	
		
		
		
		DocumentReference ref=new DocumentReference();
		
		ref.addAuthor(new Reference().setIdentifier(new Identifier().setValue("doktor kalle krank")));
		
		ref.addContent().setAttachment(
				new Attachment()
					.setTitle("content")
					.setContentType("text/plain")
					.setDataElement(new Base64BinaryType(Base64.encodeBytes(String.valueOf("bippetibapp ? ooo å").getBytes()))));
		
		String stored=ref.getContent().get(0).getAttachment().getDataElement().asStringValue();
		
		System.out.println(new String(Base64.decode(stored))+" "+stored+" ");
		System.out.println(ref.getAuthor().get(0).getIdentifier().getValue());
    }
}
