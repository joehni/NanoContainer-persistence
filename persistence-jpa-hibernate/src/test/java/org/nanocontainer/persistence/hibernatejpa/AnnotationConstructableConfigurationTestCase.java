/*****************************************************************************
 * Copyright (c) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by various                           *
 *****************************************************************************/

package org.nanocontainer.persistence.hibernatejpa;


import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.nanocontainer.persistence.pojo.Pojo;

/**
 * Constructable Configuration Test case for Annotations.
 * @author Michael Rimov
 * @author Jose Peleteiro <juzepeleteiro@intelli.biz> 
 */
public class AnnotationConstructableConfigurationTestCase extends TestCase {

    public void testDefaultConstruction() throws Exception {
        //Should Will load hibernate.hbm.xml
        AnnotationConstructableConfiguration config = new AnnotationConstructableConfiguration();
        assertNotNull(config);
        attemptWrite(config);
    }


    public void testResourceConstruction() throws Exception {
        AnnotationConstructableConfiguration config = new AnnotationConstructableConfiguration("/hibernate.cfg.xml");
        attemptWrite(config);
    }
    
    /**
     * Works Hibernate's configuration by attempting a write to the 'database'.  With the latest
     * hiberanates, the configuration isn't really built until the session factory is built, and even
     * then, some of the data doesn't exist until a write occurs.
     * @param config
     */
    private void attemptWrite(AnnotationConstructableConfiguration config) {
        Pojo pojo = new Pojo();
        pojo.setFoo("Foo!");
        
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = null;
        try {
        
            session = sessionFactory.openSession();
            Integer result = (Integer) session.save(pojo);
            assertNotNull(result);
            session.close();
        
        
            session = sessionFactory.openSession();
            Pojo pojo2 = (Pojo) session.load(Pojo.class, result);
            assertNotNull(pojo);
            assertEquals(pojo.getId(), pojo2.getId());
            assertEquals(pojo.getFoo(), pojo2.getFoo());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
            
            sessionFactory.close();
        }
    }

}
