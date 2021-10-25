package pl.qaaacademy.restasured.shop_api.models;

import java.util.Date;

public class Bill {
    private Date issueDate;
    private Date dueDate;
    private float sum;

    public Bill() {
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }
}
