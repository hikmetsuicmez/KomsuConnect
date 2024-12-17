import React, { useState } from 'react';
import PropTypes from 'prop-types';
import "../styles/AddressForm.css";

const AddressForm = ({ onSubmit }) => {
  const [addressData, setAddressData] = useState({
    contactName: '',
    city: '',
    country: '',
    address: '',
    zipCode: '',
  });

  const handleChange = (e) => {
    setAddressData({ ...addressData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(addressData);
  };

  return (
    <form onSubmit={handleSubmit} className="address-form">
      <h3>Adres Bilgileri</h3>
      <div>
        <label htmlFor="contactName">Ad Soyad:</label>
        <input
          type="text"
          id="contactName"
          name="contactName"
          value={addressData.contactName}
          onChange={handleChange}
          required
        />
      </div>
      <div>
        <label htmlFor="city">Şehir:</label>
        <input
          type="text"
          id="city"
          name="city"
          value={addressData.city}
          onChange={handleChange}
          required
        />
      </div>
      <div>
        <label htmlFor="country">Ülke:</label>
        <input
          type="text"
          id="country"
          name="country"
          value={addressData.country}
          onChange={handleChange}
          required
        />
      </div>
      <div>
        <label htmlFor="address">Adres:</label>
        <textarea
          id="address"
          name="address"
          value={addressData.address}
          onChange={handleChange}
          required
        />
      </div>
      <div>
        <label htmlFor="zipCode">Posta Kodu:</label>
        <input
          type="text"
          id="zipCode"
          name="zipCode"
          value={addressData.zipCode}
          onChange={handleChange}
          required
        />
      </div>
      <button type="submit" className="btn btn-primary">Devam Et</button>
    </form>
  );
};

AddressForm.propTypes = {
  onSubmit: PropTypes.func.isRequired,
};

export default AddressForm;

