package luceneTest2;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class testLuc {

	public static void main(String[] args) throws IOException, URISyntaxException {
		String indexPath = "/home/villalon/Escritorio/GitHub Projects/tesis-mario/LuceneTest2/index";
	    String dataPath = "/home/villalon/Escritorio/GitHub Projects/tesis-mario/LuceneTest2/data/file1.txt";
	     
	    Directory directory = FSDirectory.open(Paths.get(indexPath));
	    LuceneFileSearch luceneFileSearch = new LuceneFileSearch(directory, new StandardAnalyzer());
	     
	    luceneFileSearch.addFileToIndex(dataPath);
	     
	    List<Document> docs = luceneFileSearch.searchFiles("contents", "consectetur");
	     
	    assertEquals("file1.txt", docs.get(0).get("filename"));
	}

}
