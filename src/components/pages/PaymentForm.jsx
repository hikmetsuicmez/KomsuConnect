import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { CreditCard, Calendar, Lock } from 'react-feather';
import "../styles/PaymentForm.css";

const PaymentForm = ({ onSubmit, totalAmount }) => {
  const [paymentData, setPaymentData] = useState({
    cardHolderName: '',
    cardNumber: '',
    expireMonth: '',
    expireYear: '',
    cvc: '',
  });

  const handleChange = (e) => {
    setPaymentData({ ...paymentData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(paymentData);
  };

  // Generate month options
  const months = Array.from({ length: 12 }, (_, i) => {
    const month = i + 1;
    return (
      <option key={month} value={month.toString().padStart(2, '0')}>
        {month.toString().padStart(2, '0')}
      </option>
    );
  });

  // Generate year options (current year + 10 years)
  const currentYear = new Date().getFullYear();
  const years = Array.from({ length: 11 }, (_, i) => {
    const year = currentYear + i;
    return (
      <option key={year} value={year}>
        {year}
      </option>
    );
  });

  return (
    <div className="payment-container">
      <form onSubmit={handleSubmit} className="payment-form">
        <div className="payment-header">
          <h2>Ödeme Bilgileri</h2>
          <div className="payment-amount">
            <span>Toplam Tutar:</span>
            <span className="amount">{totalAmount} ₺</span>
          </div>
        </div>

        <div className="form-group">
          <label>
            <CreditCard size={18} />
            Kart Sahibinin Adı
          </label>
          <input
            type="text"
            name="cardHolderName"
            value={paymentData.cardHolderName}
            onChange={handleChange}
            placeholder="Ad Soyad"
            required
          />
        </div>

        <div className="form-group">
          <label>
            <CreditCard size={18} />
            Kart Numarası
          </label>
          <input
            type="text"
            name="cardNumber"
            value={paymentData.cardNumber}
            onChange={handleChange}
            placeholder="1234 5678 9012 3456"
            maxLength="16"
            required
          />
        </div>

        <div className="form-row">
          <div className="form-group expiry-date">
            <label>
              <Calendar size={18} />
              Son Kullanma Tarihi
            </label>
            <div className="expiry-inputs">
              <select
                name="expireMonth"
                value={paymentData.expireMonth}
                onChange={handleChange}
                required
              >
                <option value="">Ay</option>
                {months}
              </select>
              <select
                name="expireYear"
                value={paymentData.expireYear}
                onChange={handleChange}
                required
              >
                <option value="">Yıl</option>
                {years}
              </select>
            </div>
          </div>

          <div className="form-group cvc">
            <label>
              <Lock size={18} />
              CVC
            </label>
            <input
              type="text"
              name="cvc"
              value={paymentData.cvc}
              onChange={handleChange}
              placeholder="123"
              maxLength="3"
              required
            />
          </div>
        </div>

        <button type="submit" className="submit-button">
          Ödemeyi Tamamla
        </button>
      </form>
    </div>
  );
};

PaymentForm.propTypes = {
  onSubmit: PropTypes.func.isRequired,
  totalAmount: PropTypes.number.isRequired,
};

export default PaymentForm;

