/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestCentroSalud;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ServiciosWeb.ServiciosWebCentroSalud;

/**
 *
 * @author Pamela Palacios
 */
public class PruebaUnitaria {
    
    ServiciosWebCentroSalud request;
    public PruebaUnitaria() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
         request = new ServiciosWebCentroSalud();
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void prueba_hello()
    {
        String p="world";
        String r =  this.request.hello(p);
        assertEquals("Hello world !",r);
    }
}
