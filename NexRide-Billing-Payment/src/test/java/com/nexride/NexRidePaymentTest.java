package com.nexride;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexride.model.*;
import com.nexride.repository.FileStateRepository;
import com.nexride.service.OnlinePaymentService;
import java.nio.file.Path;
import java.math.BigDecimal;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
class NexRidePaymentTest {
 @TempDir Path temp;
 @Test void verifiesSignatureAmountOwnershipAndIdempotentPayment() throws Exception {
  try(var repo=new FileStateRepository(new ObjectMapper(),temp.toString())) {
   User user=new User();user.setName("Test Rider");user.setEmail("test@example.test");user.setPhone("+94771234567");
   Invoice invoice=new Invoice();invoice.setUserId(user.getId());invoice.setAmountLkr(8000);
   repo.update(s->{s.getUsers().add(user);s.getInvoices().add(invoice);s.getSettings().setLkrPerUsd(new BigDecimal("300"));return null;});
   var service=new OnlinePaymentService(repo,"merchant-test","secret-test","sandbox","https://example.test");
   assertThrows(RuntimeException.class,()->service.checkout(new User(),invoice.getId(),"USD","Road","City","Country"));
   var checkout=service.checkout(user,invoice.getId(),"USD","Road","City","Country");
   @SuppressWarnings("unchecked") var f=(Map<String,String>)checkout.get("fields");
   assertEquals("26.67",f.get("amount"));assertTrue(f.get("return_url").endsWith("#account"));
   Map<String,String> notification=new HashMap<>(Map.of("merchant_id","merchant-test","order_id",f.get("order_id"),"payhere_amount","26.67","payhere_currency","USD","status_code","2","payment_id","unique-payment","method","VISA","md5sig","invalid"));
   assertThrows(RuntimeException.class,()->service.notify(notification));
   notification.put("md5sig",OnlinePaymentService.md5("merchant-test"+f.get("order_id")+"26.67USD2"+OnlinePaymentService.md5("secret-test")));
   service.notify(notification);service.notify(notification);
   assertEquals(1,repo.read(s->s.getPayments().size()));assertEquals("PAID",repo.read(s->s.getInvoices().get(0).getStatus()));
  }
 }
}
