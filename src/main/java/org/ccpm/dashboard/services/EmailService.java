package org.ccpm.dashboard.services;

import java.io.File;
import java.util.List;

public interface EmailService {
     
   public void enviarEmail(String destinatario,String asunto, String mensaje, List<File> adjuntos) throws Exception;
}
