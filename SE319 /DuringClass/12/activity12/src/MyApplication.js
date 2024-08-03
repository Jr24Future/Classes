import React, { useState } from "react";
import { useForm } from "react-hook-form";

function App() {
    const { register, handleSubmit, formState: { errors } } = useForm();
    const [dataF, setDataF] = useState({});
    const [viewer, setViewer] = useState(0);
  
    const onSubmit = data => {
      setDataF(data);
      setViewer(1);
    };

  function Payment() {
    return (
      <div>
        <form onSubmit={handleSubmit(onSubmit)}>
          <input {...register("fullName", { required: true })} placeholder="Full Name" />
          {errors.fullName && <p>Full Name is required.</p>}
          <input {...register("email", { required: true, pattern: /^\S+@\S+$/i })} placeholder="Email" />
          {errors.email && <p>Email is required.</p>}
          <input {...register("creditCard", { required: true })} placeholder="Credit Card" />
          {errors.creditCard && <p>Credit Card is required.</p>}
          <input {...register("address", { required: true })} placeholder="Address" />
          {errors.address && <p>Address is required.</p>}
          <input {...register("address2")} placeholder="Address 2" />
          <input {...register("city", { required: true })} placeholder="City" />
          {errors.city && <p>City is required.</p>}
          <input {...register("state", { required: true })} placeholder="State" />
          {errors.state && <p>State is required.</p>}
          <input {...register("zip", { required: true })} placeholder="Zip" />
          {errors.zip && <p>Zip is required.</p>}
          <button type="submit">Submit</button>
        </form>
      </div>
    );
  }

  function Summary() {
    const updateHooks = () => {
      setDataF({});
      setViewer(0);
    };

    return (
        <div>
          <h1>Payment summary:</h1>
          <ul>
            <li><strong>Full Name:</strong> {dataF.fullName}</li>
            <li><strong>Email:</strong> {dataF.email}</li>
            <li><strong>Credit Card:</strong> {dataF.creditCard}</li>
            <li><strong>Address:</strong> {dataF.address}</li>
            <li><strong>Address 2:</strong> {dataF.address2}</li>
            <li><strong>City:</strong> {dataF.city}</li>
            <li><strong>State:</strong> {dataF.state}</li>
            <li><strong>Zip:</strong> {dataF.zip}</li>
          </ul>
          <button onClick={updateHooks}>Back</button>
        </div>
      );
    }
  
    return (
      <div>
        {viewer === 0 && <Payment />}
        {viewer === 1 && <Summary />}
      </div>
    );
  }

export default App;
