/*
package com.KBP.Evidence.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.KBP.Evidence.views.StatsInformationCaller;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

*/
/**
 * Created by Niranjan on 2/16/2016.
 *//*

public class KBPEvidence {

    @Autowired
    StatsInformationCaller statsInformationCaller;

    public static void main(String args[]) throws IOException, PropertyVetoException, URISyntaxException, SQLException {

        String mid_files_address=args[0];
        String freebase_result_files_address=args[1];
        String wiki_resultfiles_address=args[2];

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Freebase-Parser-app-context.xml");
        context.getBean(KBPEvidence.class).statsInformationCaller.getAllStats(mid_files_address,freebase_result_files_address,wiki_resultfiles_address);
    }
}
*/
