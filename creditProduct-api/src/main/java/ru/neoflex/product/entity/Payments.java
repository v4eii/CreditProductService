package ru.neoflex.product.entity;

import lombok.Getter;
import lombok.Setter;
import payments.schema.Payment;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payments", schema = "public")
@NamedQuery(name = "findAllPayments", query = "SELECT p FROM Payments p")
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_payment")
    @Getter
    @Setter
    private Integer idPayment;
    @Basic(optional = false)
    @Getter
    @Setter
    private float payment;
    @Basic(optional = false)
    @Column(name = "payment_body")
    @Getter
    @Setter
    private float paymentBody;
    @Basic(optional = false)
    @Column(name = "payment_percent")
    @Getter
    @Setter
    private float paymentPercent;
    @Basic(optional = false)
    @Column(name = "monthly_balance")
    @Getter
    @Setter
    private float monthlyBalance;
    @Basic(optional = false)
    @Getter
    @Setter
    private float overpayment;
    @JoinColumn(name = "id_schedule", referencedColumnName = "id_schedule")
    @ManyToOne(optional = false)
    @Getter
    @Setter
    private PaymentSchedule idSchedule;
    @Basic(optional = false)
    @Column(name = "date_payment")
    @Temporal(TemporalType.DATE)
    @Getter
    @Setter
    private Date datePayment;

    public Payments(Payment payment, PaymentSchedule paymentSchedule) {
        this.monthlyBalance = payment.getMonthlyBalance();
        this.overpayment = payment.getOverpayment();
        this.payment = payment.getPayment();
        this.paymentBody = payment.getPaymentForBody();
        this.paymentPercent = payment.getPaymentForPercent();
        datePayment = payment.getDatePayment().toGregorianCalendar().getTime();
        this.idSchedule = paymentSchedule;
    }

    public Payments() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Payments that = (Payments) o;

        if (idPayment != that.idPayment) return false;
        if (Float.compare(that.payment, payment) != 0) return false;
        if (Float.compare(that.paymentBody, paymentBody) != 0) return false;
        if (Float.compare(that.paymentPercent, paymentPercent) != 0) return false;
        if (Float.compare(that.monthlyBalance, monthlyBalance) != 0) return false;
        if (Float.compare(that.overpayment, overpayment) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idPayment;
        result = 31 * result + (payment != +0.0f ? Float.floatToIntBits(payment) : 0);
        result = 31 * result + (paymentBody != +0.0f ? Float.floatToIntBits(paymentBody) : 0);
        result = 31 * result + (paymentPercent != +0.0f ? Float.floatToIntBits(paymentPercent) : 0);
        result = 31 * result + (monthlyBalance != +0.0f ? Float.floatToIntBits(monthlyBalance) : 0);
        result = 31 * result + (overpayment != +0.0f ? Float.floatToIntBits(overpayment) : 0);
        return result;
    }
}
