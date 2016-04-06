/*
package com.KBP.Evidence.views;

import com.KBP.Evidence.Index.LuceneIndexerNewsWire;
import Nell_Extracts;
import com.KBP.Evidence.Search.LuceneQueryNewsWire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

*/
/**
 * Created by Niranjan on 2/16/2016.
 *//*

@Service
@SuppressWarnings("unchecked")
public class StatsInformationCaller {

    //Autowired the instance for all the API classes

    */
/*@Autowired
    LuceneIndexerNewsWire luceneIndexerNewsWire;
    @Autowired
    LuceneQueryNewsWire luceneQueryNewsWire;
*//*

    public void getAllStats(String mid_files_address, String result_file_address, String wiki_resultfiles_address) throws IOException, URISyntaxException, PropertyVetoException, SQLException {

        */
/*
        objects for all the stats create here statically
       */
/*  *//*
*/
/*
        Logger logger = LoggerFactory.getLogger(StatsInformationCaller.class);

        //Calling the method to get ad group action stats

      *//*
*/
/*  boolean finishedFbStats = freebaseStats.getOverAllFBstats(mid_files_address, result_file_address);

        if (finishedFbStats) {
            System.out.println("Freebase API MID extraction Completed Successfully");

        }
*//*
*/
/*
        boolean finishedWikiStats = luceneIndexerNewsWire.getWikitext(result_file_address, wiki_resultfiles_address);

        if (finishedWikiStats) {
            System.out.println("Wikipedia API Text extraction Completed Successfully");

        }*//*

    }


}
*/
