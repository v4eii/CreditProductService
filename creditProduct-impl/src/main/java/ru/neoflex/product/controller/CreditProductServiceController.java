package ru.neoflex.product.controller;

import com.sun.net.httpserver.Headers;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import payments.schema.Credit;
import payments.schema.Payment;
import ru.neoflex.product.entity.CreditProduct;
import ru.neoflex.product.entity.Payments;
import ru.neoflex.product.service.CreditProductServiceImpl;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.util.*;

@RestController
public class CreditProductServiceController {

    private static final Logger LOG = LoggerFactory.getLogger(CreditProductServiceController.class);

    @Autowired
    private CreditProductServiceImpl creditProductService;

    @Autowired
    private KafkaTemplate<Long, Credit> kafkaTemplate;

    @RequestMapping(method = RequestMethod.POST, path = "/findProduct")
    public CreditProduct getSuitableProduct(
            @RequestParam(required = true, value = "price") Long price,
            @RequestParam(required = true, value = "term") Integer term,
            @RequestParam(value = "isIssue", defaultValue = "false") Boolean isIssue) {
        List<CreditProduct> suitableCreditProduct = creditProductService.findSuitableCreditProduct(price, term);
        if (!suitableCreditProduct.isEmpty()) {
            if (isIssue) {
                try {
                    Credit c = new Credit();
                    c.setId(0);
                    c.setDateIssue(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(Calendar.getInstance().getTimeZone())));
                    c.setPercent(suitableCreditProduct.get(0).getPercent());
                    c.setPrice(new BigDecimal(suitableCreditProduct.get(0).getMaxPrice()));
                    c.setTerm(suitableCreditProduct.get(0).getMinTerm());
                    c.setType(suitableCreditProduct.get(0).getType());

                    ListenableFuture<SendResult<Long, Credit>> future = kafkaTemplate.send("credit", c.getId(), c);
                    future.addCallback(System.out::println, System.err::println);
                    kafkaTemplate.flush();
                }
                catch (DatatypeConfigurationException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            return suitableCreditProduct.get(0);
        }
        return null;
    }

    // Не знаю почему передаваемый List<Payment> при сериализации и десериализации преобразуется в List<LinkedHashMap>
    @KafkaListener(topics = "resultList")
    public void msgListener(ConsumerRecord<Long, List<LinkedHashMap>> record) {
        List<LinkedHashMap> value = record.value();
        for (LinkedHashMap val : value) {
            System.out.println("#");
            System.out.println(val.get("paymentForBody"));
            System.out.println(val.get("paymentForPercent"));
        }
    }

}
