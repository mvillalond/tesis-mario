package luceneTest2;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexDeletionPolicy;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.KeepOnlyLastCommitDeletionPolicy;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

public class testLuc {

	public static void main(String[] args) throws IOException, URISyntaxException, ParseException {
		/*String indexPath = "/home/villalon/Escritorio/GitHub Projects/tesis-mario/LuceneTest2/index";
	    String dataPath = "/home/villalon/Escritorio/GitHub Projects/tesis-mario/LuceneTest2/data/file1.txt";
	     
	    Directory directory = FSDirectory.open(Paths.get(indexPath));
	    LuceneFileSearch luceneFileSearch = new LuceneFileSearch(directory, new StandardAnalyzer());
	     
	    luceneFileSearch.addFileToIndex(dataPath);
	     
	    List<Document> docs = luceneFileSearch.searchFiles("contents", "consectetur");
	     
	    assertEquals("file1.txt", docs.get(0).get("filename"));
		
		//Create instance of Directory where index files will be stored
		//String data = "/home/villalon/Escritorio/GitHub Projects/tesis-mario/LuceneTest2/data";
		//File indexDirectory = new File(data);
		@SuppressWarnings("deprecation")
		Directory fsDirectory =  new RAMDirectory();
		// Create instance of analyzer, which will be used to tokenize the input data 
		Analyzer standardAnalyzer = new StandardAnalyzer();
		//Create a new index
		boolean create = true;
		//Create the instance of deletion policy
		IndexDeletionPolicy deletionPolicy = new KeepOnlyLastCommitDeletionPolicy();
		IndexWriter indexWriter =new IndexWriter(fsDirectory, new IndexWriterConfig(standardAnalyzer));*/
		// 0. Specify the analyzer for tokenizing text.
        //    The same analyzer should be used for indexing and searching
        StandardAnalyzer analyzer = new StandardAnalyzer();

        // 1. create the index
        Directory index = new RAMDirectory();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        IndexWriter w = new IndexWriter(index, config);
        addDoc(w, "Lucene in Action", "193398817");
        addDoc(w, "Lucene for Dummies", "55320055Z");
        addDoc(w, "Managing Gigabytes", "55063554A");
        addDoc(w, "The Art of Computer Science", "9900333X");
        w.close();

        // 2. query
        String querystr = args.length > 0 ? args[0] : "Computer";

        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.
        Query q = new QueryParser("title", analyzer).parse(querystr);

        // 3. search
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;

        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title"));
        }

        // reader can only be closed when there
        // is no need to access the documents any more.
        reader.close();
    }

    private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));

        // use a string field for isbn because we don't want it tokenized
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        w.addDocument(doc);
    }
    
    private Document getDocument(File file) throws IOException{
    	Document document = new Document();
    	
    	//index file contents
    	Field contentField = new Field("content", new FileReader(file), TextField.TYPE_STORED);
    	Field fileNameField = new Field("filename", file.getName(), TextField.TYPE_STORED);
    	Field filePathField = new Field("filepath", file.getCanonicalPath(), TextField.TYPE_STORED);
    	
    	document.add(contentField);
    	document.add(fileNameField);
    	document.add(filePathField);
    	
    	return document;
    	
    }
}

