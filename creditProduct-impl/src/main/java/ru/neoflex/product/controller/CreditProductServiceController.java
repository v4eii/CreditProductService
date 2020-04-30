package ru.neoflex.product.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import payments.schema.Credit;
import payments.schema.Payment;
import ru.neoflex.product.entity.CreditProduct;
import ru.neoflex.product.messaging.ApplicationSink;
import ru.neoflex.product.service.CreditProductServiceImpl;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@RestController
@EnableBinding(ApplicationSink.class)
public class CreditProductServiceController {

    private static final Logger LOG = LoggerFactory.getLogger(CreditProductServiceController.class);

    @Autowired
    private CreditProductServiceImpl creditProductService;

    @Autowired
    private ApplicationSink sink;

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

                    Message<Credit> msg = MessageBuilder.withPayload(c).setHeader(KafkaHeaders.MESSAGE_KEY, c.getType()).build();
                    sink.output().send(msg);
                }
                catch (DatatypeConfigurationException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            return suitableCreditProduct.get(0);
        }
        return null;
    }

    @StreamListener(target = ApplicationSink.INPUT)
    public void on(@Payload List<Payment> paymentList) {
        System.out.println(paymentList.size());
    }

}
