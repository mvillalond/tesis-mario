package cl.unap.icci.terrierTest;

/**
 * Hello world!
 *
 */

import java.io.File; 
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap; 
import org.terrier.indexing.Collection; 
import org.terrier.indexing.SimpleFileCollection; 
import org.terrier.structures.indexing.Indexer; 
import org.terrier.structures.Index;
import org.terrier.querying.*; 
import org.terrier.structures.indexing.classical.BasicIndexer;
import org.terrier.utility.ApplicationSetup;

public class App 
{
    public static void main( String[] args ) throws Exception {
    	// Directory containing files to index
        String aDirectoryToIndex = "C:\\Users\\zaess\\Desktop\\probando\\docs\\";

        // Configure Terrier
        ApplicationSetup.setProperty("indexer.meta.forward.keys", "test.html");
        ApplicationSetup.setProperty("indexer.meta.forward.keylens", "200");

        Indexer indexer = new BasicIndexer("C:\\Users\\zaess\\Desktop\\probando\\path\\", "data");
        Collection coll = new SimpleFileCollection(Arrays.asList(aDirectoryToIndex), true);
        indexer.index(new Collection[]{coll});
        //indexer.close();

        Index index = Index.createIndex("C:\\Users\\zaess\\Desktop\\probando\\path\\", "data");

        // Enable the decorate enhancement
        ApplicationSetup.setProperty("querying.postfilters", "decorate:org.terrier.querying.SimpleDecorate");

        // Create a new manager run queries
        Manager queryingManager = ManagerFactory.from(index.getIndexRef());

        // Create a search request
        SearchRequest srq = queryingManager.newSearchRequestFromQuery("document");

        // Specify the model to use when searching
        srq.setControl(SearchRequest.CONTROL_WMODEL, "BM25");

        // Turn on decoration for this search request
        srq.setControl("decorate", "on");

        // Run the search
        queryingManager.runSearchRequest(srq);

        // Get the result set
        ScoredDocList results = srq.getResults();

        // Print the results
        System.out.println("The top "+results.size()+" of documents were returned");
        System.out.println("Document Ranking");
        int i = 0;
        for(ScoredDoc doc : results) {
        	i++;
            int docid = doc.getDocid();
            double score = doc.getScore();
            String docno = doc.getMetadata("docno");
            System.out.println("   Rank "+i+": "+docid+" "+docno+" "+score);
        }
    }
}
