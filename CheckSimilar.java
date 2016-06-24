package minhash;

import java.util.Set;

public class CheckSimilar {
	
	private String author;
	private String title;
	private String barCode;
	private String lang;
	
	public void readMetadata(String filename1)
	{
		MetadataReader file1 = new MetadataReader(filename1);
    	file1.process();
    	author = file1.retAuthor();
    	title = file1.retTitle();
    	barCode = file1.retBarcode();
    	lang = file1.retLang();
	}
	
	public static double retSimilarity(CheckSimilar A, CheckSimilar B)
	{
		double smlr = 0.0;
		
		//author
		Set<String> author_A = MinHash.createShinglesWords(A.author, 1);
		Set<String> author_B = MinHash.createShinglesWords(B.author, 1);
		
		//title
		Set<String> title_A = MinHash.createShinglesWords(A.title, 2);
		Set<String> title_B = MinHash.createShinglesWords(B.title, 2);
		
		//barcode
		Set<String> barcode_A = MinHash.createShinglesWords(A.barCode, 1);
		Set<String> barcode_B = MinHash.createShinglesWords(B.barCode, 1);
		
		//language
		Set<String> lang_A = MinHash.createShinglesWords(A.lang, 1);
		Set<String> lang_B = MinHash.createShinglesWords(B.lang, 1);
		
		MinHash<String> minHash1 = new MinHash<String>(author_A.size() + author_B.size());
        smlr = minHash1.similarity(author_A, author_B);
        
        MinHash<String> minHash2 = new MinHash<String>(title_A.size() + title_B.size());
        smlr = smlr * minHash2.similarity(title_A, title_B);
        
        MinHash<String> minHash3 = new MinHash<String>(barcode_A.size() + barcode_B.size());
        smlr = smlr * minHash3.similarity(barcode_A, barcode_B);
        
        MinHash<String> minHash4 = new MinHash<String>(lang_A.size() + lang_B.size());
        smlr = smlr * minHash4.similarity(lang_A, lang_B);
        
		return smlr;
	}
	
	public static void main(String[] args)
	{
		CheckSimilar file1 = new CheckSimilar();
		CheckSimilar file2 = new CheckSimilar();
		
		file1.readMetadata("metadata1.xml");
		file2.readMetadata("metadata2.xml");
		
		System.out.println("Similarity: " + retSimilarity(file1, file2));		
	}
}
