package reports.readers;

import java.util.function.Supplier;
import java.util.regex.Pattern;

import util.ChainMatch;
import util.Matcher;

/*
*
*   TROMSØ Kommune 05.03.2021 13:41:10
	Rapport kjørt av: Torbjørn Torsvik Utskrift av pasientjournal Side 11 / 356
	Bruker: Kalle Krank
	Fødselsdato / Persnr: 30.10.1987 / 56144
	Plan/rapport
	Plankategori: Helsehjelp
	Planområde: Grunnleggende behov
	Tiltak: Personlig stell 04.07.2019 - Skrevet av: Torbjørn Torsvik
	Endret Av: Torbjørn Torsvik
	Fra - Til: 23.07.2019 - 29.10.2019
	Foreløpig tiltak:
	Lorem Ipsum
	Endret Av: Torbjørn Torsvik
	Fra - Til: 23.07.2019 - 23.07.2019
	Foreløpig tiltak:
	Lorem Ipsum .
	Endret Av: Torbjørn Torsvik
	Fra - Til: 23.07.2019 - 23.07.2019
	Foreløpig tiltak:
	Lorem Ipsum
	Endret Av: Torbjørn Torsvik
	
	TROMSØ Kommune 05.03.2021 13:41:10
	Rapport kjørt av: Torbjørn Torsvik Utskrift av pasientjournal Side 11 / 356
	Bruker: Kalle Krank
	Fødselsdato / Persnr: 30.10.1987 / 56144
	Plan/rapport
	Plankategori: Helsehjelp
	Planområde: Grunnleggende behov
	Tiltak: Personlig stell 04.07.2019 - Skrevet av: Torbjørn Torsvik
	Fra - Til: 04.07.2019 - 22.07.2019
	Endret Av: Torbjørn Torsvik
	Fra - Til: 30.10.2019 - 21.02.2021
	Lorem ipsum
	Bakgrunn for tiltaket:
	Grundig beskrivelse av hva som gjøres når, både med tanke på pasientens ressurser og tidspunkter for gjøremål.
	Endret dato: 22.02.2021
	Lorem Ipsum
	Rapporter: 904
	1 03.03.2021 Skrevet av: Torbjørn Torsvik Rapport Rapport dato: 03.03.2021
	Vakt: Aftenvakt Status: Uendret Endre tiltak: Nei Prioritet gitt: Nei
	Rapport: Lorem Ipsum
	
*  
*  
*  
*  TROMSØ Kommune 05.03.2021 13:41:10
	Rapport kjørt av: Torbjørn Torsvik Utskrift av pasientjournal Side 11 / 356
	Bruker: Kalle Krank
	Fødselsdato / Persnr: 30.10.1987 / 56144
	Plan/rapport
	Plankategori: Helsehjelp
	Planområde: Grunnleggende behov
	Tiltak: Personlig stell 04.07.2019 - Skrevet av: Torbjørn Torsvik
	morgenmedisin, ikke vært på kjøkkenet idag og sa hun ikke skulle komme til middag heller.
	19 17.02.2021 Skrevet av: Torbjørn Torsvik Rapport Rapport dato: 17.02.2021
*/

public class ReportStartMatcher {
	private static final Supplier<Matcher> startMatcher=()->new ChainMatch.Builder()
			.addPattern(Pattern.compile("^Plan/Rapport.*"))
			.addPattern(Pattern.compile("^Plankategori:.*"))
			.addPattern(Pattern.compile("^Planområde:.*"))
			.addPattern(Pattern.compile("^Tiltak:.*"))
			.build();
	
	
}
