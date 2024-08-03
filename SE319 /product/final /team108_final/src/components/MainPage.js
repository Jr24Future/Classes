import React, { useState, useRef, useEffect } from 'react';
import Navbar from './Navbar';
import '../App.css';
import materials from "../materials.json";
import Carousel from 'react-bootstrap/Carousel';
import axios from 'axios';
import Footer from './Footer';

function MainPage({ userId }) {
    const [isBuying, setIsBuying] = useState([]);
    const [count, setCount] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [searchSubmitted, setSearchSubmitted] = useState(false);
    const [totalCount, setTotalCount] = useState(0);
    const [totalPrice, setTotalPrice] = useState(0);
    const [showCartView, setShowCartView] = useState(1);
    const [cardNumber, setCardNumber] = useState('');
    const [addressLine, setAddressLine] = useState('');
    const [city, setCity] = useState('');
    const [state, setState] = useState('');
    const [zipCode, setZipCode] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [formErrors, setFormErrors] = useState({});
    const [selectedCategory, setSelectedCategory] = useState("");  
    const [index, setIndex] = useState(0);
    const [activeTab, setActiveTab] = useState(0);
    const tabsRef = useRef(null);
    const [underlineStyle, setUnderlineStyle] = useState({});
    const [products, setProducts] = useState([]); 

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await axios.get('http://localhost:8081/product');
                setProducts(response.data);
                setIsBuying(new Array(response.data.length).fill(false));
                setCount(new Array(response.data.length).fill(1));
            } catch (error) {
                console.error('Error fetching products:', error);
            }
        };
        fetchProducts();
    }, []);

    useEffect(() => {
        if (products.length > 1) {
            setSelectedCategory(products[1].category);
        }
    }, [products]); 

    useEffect(() => {
        const tab = tabsRef.current.children[activeTab];
        setUnderlineStyle({
            width: `${tab.offsetWidth}px`,
            left: `${tab.offsetLeft}px`
        });
    }, [activeTab]);

    const handleTabChange = (newTabIndex) => {
        setActiveTab(newTabIndex);
    };

    const handleSelect = (selectedIndex) => {
        setIndex(selectedIndex);
    };

    const filteredProducts = products.filter(product => product.category === selectedCategory);

    const handleCategorySelect = (category) => {
        setSelectedCategory(category);
        console.log("Selected Category:", category);
    };
    
    const resetState = () => {
        setIsBuying(new Array(products.length).fill(false));
        setCount(new Array(products.length).fill(1));
        setTotalCount(0);
        setTotalPrice(0);
    };

    // Function to toggle the cart view
    const toggleCartView = (page) => {
        setShowCartView(page);
    };
    
    const handleOrderClick = () => {
        const validationErrors = validateInputs();
        if (Object.keys(validationErrors).length > 0) {
            setFormErrors(validationErrors); // Set errors state
            return;
        }
    
        setFormErrors({}); // Clear errors if validation is successful
        console.log('Processing order with:', {cardNumber, addressLine, city, state, zipCode, firstName, lastName, email, phoneNumber});
        toggleCartView(3);
    };  
    
    const validateInputs = () => {
        const errors = {};
    
        // Validate card number
        if (!/^\d{16}$/.test(cardNumber)) {
            errors.cardNumber = 'Card number must be 16 digits.';
        }
    
        // Validate zip code
        if (!/^\d{5}$/.test(zipCode)) {
            errors.zipCode = 'Zip code must be 5 digits.';
        }

        // Validate phone number
        if (!/^\d{10}$/.test(phoneNumber)) {
            errors.phoneNumber = 'Phone number must be 10 digits.';
        }  

        // Validate other fields (non-empty)
        if (!addressLine.trim()) {
            errors.addressLine = 'Address line is required.';
        }
        if (!city.trim()) {
            errors.city = 'City is required.';
        }
        if (!state.trim()) {
            errors.state = 'State is required.';
        }
        if (!firstName.trim()) {
            errors.firstName = 'First name is required.';
        }
        if (!lastName.trim()) {
            errors.lastName = 'Last name is required.';
        }
        if (!email.trim()) {
            errors.email = 'Email is required.';
        }
    
        return errors;
    };

    // Function to handle clicking the "Buy Now" button for a specific card
    const handleBuyNow = (index) => {
        const updatedIsBuying = [...isBuying];
        updatedIsBuying[index] = true;
        setIsBuying(updatedIsBuying);
        setTotalCount(prevTotalCount => prevTotalCount + 1);
        setTotalPrice(prevTotalPrice => parseFloat((parseFloat(prevTotalPrice) + parseFloat(products[index].price)).toFixed(2)));
    };

    // Function to increment the count for a specific card
    const incrementCount = (index) => {
        const updatedCount = [...count];
        updatedCount[index] += 1;
        setCount(updatedCount);
        setTotalCount(prevTotalCount => prevTotalCount + 1);
        setTotalPrice(prevTotalPrice => parseFloat((parseFloat(prevTotalPrice) + parseFloat(products[index].price)).toFixed(2)));
    };

    // Function to decrement the count and possibly stop buying for a specific card
    const decrementCount = (index) => {
        const updatedCount = [...count];
        if (updatedCount[index] > 1) {
            updatedCount[index] -= 1;
        } 
        else {
            const updatedIsBuying = [...isBuying];
            updatedIsBuying[index] = false;
            setIsBuying(updatedIsBuying);
        }
        setCount(updatedCount);
        setTotalCount(prevTotalCount => prevTotalCount - 1);
        setTotalPrice(prevTotalPrice => parseFloat((parseFloat(prevTotalPrice) - parseFloat(products[index].price)).toFixed(2)));
    };

    if (showCartView === 1) {
        return (
          <>
            <header className="header">
              <Navbar products={products} onSelectCategory={handleCategorySelect} />
              <button onClick={() => toggleCartView(2)}>
                <span className="fa-stack has-badge" data-count={totalCount} style={{ fontSize: '24px' }}>
                  <i className="fa fa-shopping-cart fa-stack-2x red-cart"></i>
                </span>
              </button>
            </header>
    
            <div style={{
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              height: '100vh',
              width: '100%'
            }}>
              <div style={{
                backgroundColor: '#3b3838',
                width: '80%',
                maxWidth: '960px',
                height: '500px', 
                borderRadius: '10px',
                overflow: 'hidden',
                boxShadow: '0 2px 4px rgba(0,0,0,0.5)',
                position: 'relative'
              }}>
                <Carousel activeIndex={index} onSelect={handleSelect} interval={3000} indicators={true} prevLabel="" nextLabel="">
                  {filteredProducts.length > 0 ? (
                    filteredProducts.map(product => (
                      <Carousel.Item key={product.id} style={{ width: '100%', height: '500px' }}>
                        <img
                          src={product.image}
                          alt="Displayed Product"
                          style={{ width: '100%', height: '100%', objectFit: 'contain' }}
                        />
                      </Carousel.Item>
                    ))
                  ) : (
                    <Carousel.Item style={{ width: '100%', height: '500px' }}>
                      <img
                        src="/images/amethyst.png"
                        alt="Default Image"
                        style={{ width: '100%', height: '100%', objectFit: 'contain' }}
                      />
                    </Carousel.Item>
                  )}
                </Carousel>
              </div>
            </div>
    
            <div className="tabs" ref={tabsRef}>
              {["Products", "Description"].map((tab, index) => (
                <button
                  key={index}
                  onClick={() => handleTabChange(index)}
                  className={`tab-button ${activeTab === index ? "active" : ""}`}
                >
                  {tab}
                </button>
              ))}
              <div className="active-underline" style={underlineStyle}></div>
            </div>
    
            <br></br>
      
            <div className="tab-content">
              {activeTab === 0 ? (
                <div>
                  <form
                    className="flex items-start max-w-sm mb-5" 
                    onSubmit={(e) => {
                      e.preventDefault(); 
                      setSearchSubmitted(true); 
                    }}      
                  >
                    <label for="simple-search" class="sr-only">Search</label>
                      <div class="relative w-full">
                          <div class="absolute inset-y-0 start-0 flex items-center ps-3 pointer-events-none">
                              <svg class="w-5 h-5 text-gray-500 dark:text-gray-400" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 18 20">
                                  <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5v10M3 5a2 2 0 1 0 0-4 2 2 0 0 0 0 4Zm0 10a2 2 0 1 0 0 4 2 2 0 0 0 0-4Zm12 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4Zm0 0V6a3 3 0 0 0-3-3H9m1.5-2-2 2 2 2"/>
                              </svg>
                          </div>
                          <input
                            type="text"
                            id="simple-search"
                            className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg block w-full pl-10 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white focus:outline-none"
                            placeholder="Search product name..."
                            value={searchQuery}
                            onChange={(e) => {
                              setSearchQuery(e.target.value);
                              setSearchSubmitted(false);
                            }}                  
                            required
                          />
                      </div>
                      <button type="submit" class="p-2.5 ms-2 mt-0.5 text-sm font-medium text-black bg-white rounded-lg border border-gray-300 hover:bg-gray-100 focus:ring-4 focus:outline-none focus:ring-gray-200 dark:border-gray-600 dark:hover:bg-gray-700 dark:focus:ring-gray-800">
                        <svg class="w-4 h-4" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
                            <path stroke="black" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"/>
                        </svg>
                        <span class="sr-only">Search</span>
                      </button>
                  </form>
                  <main className="container">
                    {products.map((product, index) => {
                      if (!searchSubmitted || product.name.toLowerCase().includes(searchQuery.toLowerCase())) {
                        if (product.category === selectedCategory) {
                          return (
                            <section key={index} className="card">
                              <div className="product-image">
                                <img src={product.image} alt={product.name} draggable="false" />
                              </div>
                              <div className="product-info">
                                <h2>{product.name}</h2>
                                <p>{product.description}</p>
                                <div className="price">{`$${product.price}`}</div>
                              </div>
                              <div className="btn">
                                {!isBuying[index] ? (
                                  <button className="buy-btn" onClick={() => handleBuyNow(index)}>Add to Cart</button>
                                ) : (
                                  <div className="flex items-center justify-center">
                                    <button className="counter-btn" onClick={() => decrementCount(index)} style={{ backgroundColor: product.color }}>
                                      <svg className="w-4 h-4" fill="none" stroke="#ffffff" viewBox="0 0 24 24"
                                        xmlns="http://www.w3.org/2000/svg">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M20 12H4"></path>
                                      </svg>
                                    </button>
                                    <span className="text-2xl font-bold mx-4" style={{ color: '#fbf7f7', cursor: 'default' }}>{count[index]}</span>
                                    <button className="counter-btn" onClick={() => incrementCount(index)} style={{ backgroundColor: product.color }}>
                                      <svg className="w-4 h-4" fill="none" stroke="#ffffff" viewBox="0 0 24 24"
                                          xmlns="http://www.w3.org/2000/svg">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 6v12m-6-6h12"></path>
                                      </svg>
                                    </button>
                                  </div>
                                )}
                              </div>
                            </section>
                          );
                        }
                      }
                      return null;
                    })}
                  </main>
                </div>
              ) : (
                materials.find(material => material.name === selectedCategory) ?
                  <div>
                    <h1 style={{ textAlign: 'center', fontWeight: 'bold', marginBottom: '30px', fontSize: '15pt' }}>
                      {selectedCategory}
                    </h1>
                    <p>
                      &nbsp;&nbsp;
                      {materials.find(material => material.name === selectedCategory).description.split('\n').map((line, index) => (
                        <React.Fragment key={index}>
                          {line} <br /><br />&nbsp;&nbsp;
                        </React.Fragment>
                      ))}
                    </p>
                  </div>
                  :
                  <p>No description available.</p>
              )}          
            </div>
    
            <br></br>
            <br></br>
            <br></br>
            <Footer userId={userId} />
          </>
        );
      }
      else if (showCartView === 2) {
        return (
          <div class="font-[sans-serif] bg-gray-50">
          <div class="grid lg:grid-cols-2 xl:grid-cols-3 gap-4 h-full">
            <div class="bg-[#3f3f3f] lg:h-screen lg:sticky lg:top-0">
              <div class="relative h-full">
                <div class="p-8 lg:overflow-auto lg:h-[calc(100vh-60px)]">
                  <h2 class="text-2xl font-bold text-white">Order Summary</h2>
                  <div class="space-y-6 mt-10">
                    {products.map((product, index) => (
                      isBuying[index] && (
                        <div class="grid sm:grid-cols-2 items-start gap-6" key={index}>
                          <div class="px-4 py-6 shrink-0 bg-gray-50 rounded-md">
                            <img src={product.image} class="w-full object-contain" alt={product.name} />
                          </div>
                          <div>
                            <h3 class="text-base text-white">{product.name}</h3>
                            <ul class="text-xs text-white space-y-3 mt-4">
                              <li class="flex flex-wrap gap-4">Quantity <span class="ml-auto">{count[index]}</span></li>
                              <li class="flex flex-wrap gap-4">Total Price <span class="ml-auto">{`$${(product.price * count[index]).toFixed(2)}`}</span></li>
                            </ul>
                          </div>
                        </div>
                      )
                    ))}
                  </div>
                </div>
                <div class="absolute left-0 bottom-0 bg-[#444] w-full p-4">
                  <h4 class="flex flex-wrap gap-4 text-base text-white">Total <span class="ml-auto">${totalPrice}</span></h4>
                </div>
              </div>
            </div>
            <div class="xl:col-span-2 h-max rounded-md p-8 sticky top-0 relative">
              <h2 class="text-2xl font-bold text-[#333]">Complete your order</h2>
              <button onClick={() => toggleCartView(1)} type="button" class="absolute top-7 right-7 bg-transparent p-0">
                <img src="/images/home.png" alt="Home" class="w-8 h-8" />
              </button>
              <form class="mt-10">
                <div>
                <h3 class="text-lg font-bold text-[#333] mb-6">Personal Details</h3>
                <div className="flex flex-wrap gap-4">
                  {formErrors.firstName && <p className="text-red-500">{formErrors.firstName}</p>}
                  {formErrors.lastName && <p className="text-red-500">{formErrors.lastName}</p>}
                  {formErrors.email && <p className="text-red-500">{formErrors.email}</p>}
                  {formErrors.phoneNumber && <p className="text-red-500">{formErrors.phoneNumber}</p>}
                </div>
                  <br></br>
                  <div class="grid sm:grid-cols-2 gap-6">
                    <div class="relative flex items-center">
                      <input 
                        type="text" 
                        placeholder="First Name"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                        class="px-4 py-3.5 bg-white text-[#333] w-full text-sm border-b-2 focus:border-[#333] outline-none" />
                      <svg xmlns="http://www.w3.org/2000/svg" fill="#bbb" stroke="#bbb" class="w-[18px] h-[18px] absolute right-4"
                        viewBox="0 0 24 24">
                        <circle cx="10" cy="7" r="6" data-original="#000000"></circle>
                        <path
                          d="M14 15H6a5 5 0 0 0-5 5 3 3 0 0 0 3 3h12a3 3 0 0 0 3-3 5 5 0 0 0-5-5zm8-4h-2.59l.3-.29a1 1 0 0 0-1.42-1.42l-2 2a1 1 0 0 0 0 1.42l2 2a1 1 0 0 0 1.42 0 1 1 0 0 0 0-1.42l-.3-.29H22a1 1 0 0 0 0-2z"
                          data-original="#000000"></path>
                      </svg>
                    </div>
                    <div class="relative flex items-center">
                      <input 
                        type="text" 
                        placeholder="Last Name"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                        class="px-4 py-3.5 bg-white text-[#333] w-full text-sm border-b-2 focus:border-[#333] outline-none" />
                      <svg xmlns="http://www.w3.org/2000/svg" fill="#bbb" stroke="#bbb" class="w-[18px] h-[18px] absolute right-4"
                        viewBox="0 0 24 24">
                        <circle cx="10" cy="7" r="6" data-original="#000000"></circle>
                        <path
                          d="M14 15H6a5 5 0 0 0-5 5 3 3 0 0 0 3 3h12a3 3 0 0 0 3-3 5 5 0 0 0-5-5zm8-4h-2.59l.3-.29a1 1 0 0 0-1.42-1.42l-2 2a1 1 0 0 0 0 1.42l2 2a1 1 0 0 0 1.42 0 1 1 0 0 0 0-1.42l-.3-.29H22a1 1 0 0 0 0-2z"
                          data-original="#000000"></path>
                      </svg>
                    </div>
                    <div class="relative flex items-center">
                      <input 
                        type="text" 
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        class="px-4 py-3.5 bg-white text-[#333] w-full text-sm border-b-2 focus:border-[#333] outline-none" />
                      <svg xmlns="http://www.w3.org/2000/svg" fill="#bbb" stroke="#bbb" class="w-[18px] h-[18px] absolute right-4"
                        viewBox="0 0 682.667 682.667">
                        <defs>
                          <clipPath id="a" clipPathUnits="userSpaceOnUse">
                            <path d="M0 512h512V0H0Z" data-original="#000000"></path>
                          </clipPath>
                        </defs>
                        <g clip-path="url(#a)" transform="matrix(1.33 0 0 -1.33 0 682.667)">
                          <path fill="none" stroke-miterlimit="10" stroke-width="40"
                            d="M452 444H60c-22.091 0-40-17.909-40-40v-39.446l212.127-157.782c14.17-10.54 33.576-10.54 47.746 0L492 364.554V404c0 22.091-17.909 40-40 40Z"
                            data-original="#000000"></path>
                          <path
                            d="M472 274.9V107.999c0-11.027-8.972-20-20-20H60c-11.028 0-20 8.973-20 20V274.9L0 304.652V107.999c0-33.084 26.916-60 60-60h392c33.084 0 60 26.916 60 60v196.653Z"
                            data-original="#000000"></path>
                        </g>
                      </svg>
                    </div>
                    <div class="relative flex items-center">
                      <input 
                        type="text" 
                        placeholder="Phone No."
                        value={phoneNumber}
                        onChange={(e) => setPhoneNumber(e.target.value)}
                        class="px-4 py-3.5 bg-white text-[#333] w-full text-sm border-b-2 focus:border-[#333] outline-none" />
                      <svg fill="#bbb" class="w-[18px] h-[18px] absolute right-4" viewBox="0 0 64 64">
                        <path
                          d="m52.148 42.678-6.479-4.527a5 5 0 0 0-6.963 1.238l-1.504 2.156c-2.52-1.69-5.333-4.05-8.014-6.732-2.68-2.68-5.04-5.493-6.73-8.013l2.154-1.504a4.96 4.96 0 0 0 2.064-3.225 4.98 4.98 0 0 0-.826-3.739l-4.525-6.478C20.378 10.5 18.85 9.69 17.24 9.69a4.69 4.69 0 0 0-1.628.291 8.97 8.97 0 0 0-1.685.828l-.895.63a6.782 6.782 0 0 0-.63.563c-1.092 1.09-1.866 2.472-2.303 4.104-1.865 6.99 2.754 17.561 11.495 26.301 7.34 7.34 16.157 11.9 23.011 11.9 1.175 0 2.281-.136 3.29-.406 1.633-.436 3.014-1.21 4.105-2.302.199-.199.388-.407.591-.67l.63-.899a9.007 9.007 0 0 0 .798-1.64c.763-2.06-.007-4.41-1.871-5.713z"
                          data-original="#000000"></path>
                      </svg>
                    </div>
                  </div>
                </div>
                <br></br>
                <div class="mt-6">
                  <h3 class="text-lg font-bold text-[#333] mb-6">Payment Information</h3>
                  <div className="flex flex-wrap gap-4">
                    {formErrors.cardNumber && <p className="text-red-500">{formErrors.cardNumber}</p>}
                    {formErrors.addressLine && <p className="text-red-500">{formErrors.addressLine}</p>}
                    {formErrors.city && <p className="text-red-500">{formErrors.city}</p>}
                    {formErrors.state && <p className="text-red-500">{formErrors.state}</p>}
                    {formErrors.zipCode && <p className="text-red-500">{formErrors.zipCode}</p>}
                  </div>
                  <br></br>
                  <div class="grid sm:grid-cols-2 gap-6">
                    <input 
                      type="text" 
                      placeholder="Card Number"
                      value={cardNumber}
                      onChange={(e) => setCardNumber(e.target.value)}
                      class="px-4 py-3.5 bg-white text-[#333] w-full text-sm border-b-2 focus:border-[#333] outline-none" 
                    />
                    <input 
                      type="text" 
                      placeholder="Address Line"
                      value={addressLine}
                      onChange={(e) => setAddressLine(e.target.value)}
                      class="px-4 py-3.5 bg-white text-[#333] w-full text-sm border-b-2 focus:border-[#333] outline-none" 
                    />
                    <input 
                      type="text" 
                      placeholder="City"
                      value={city}
                      onChange={(e) => setCity(e.target.value)}
                      class="px-4 py-3.5 bg-white text-[#333] w-full text-sm border-b-2 focus:border-[#333] outline-none" 
                    />
                    <input 
                      type="text" 
                      placeholder="State"
                      value={state}
                      onChange={(e) => setState(e.target.value)}
                      class="px-4 py-3.5 bg-white text-[#333] w-full text-sm border-b-2 focus:border-[#333] outline-none" 
                    />
                    <input 
                      type="text" 
                      placeholder="Zip Code"
                      value={zipCode}
                      onChange={(e) => setZipCode(e.target.value)}
                      class="px-4 py-3.5 bg-white text-[#333] w-full text-sm border-b-2 focus:border-[#333] outline-none" 
                    />
                  </div>
                  <div className="flex gap-6 justify-center max-sm:flex-col mt-10">
                    <button onClick={handleOrderClick} type="button" className="rounded-md px-12 py-3 text-sm font-semibold bg-[#333] text-white hover:bg-[#222]">Order</button>
                  </div>
                </div>
              </form>
            </div>
          </div>
          <Footer />
        </div>
        );
      }
      else if (showCartView === 3) {
        return (
          <div className="flex justify-center items-center min-h-screen bg-gray-100">
            <button onClick={() => { toggleCartView(1); resetState(); }} type="button" className="absolute top-7 right-7 bg-transparent p-0">
              <img src="/images/home.png" alt="Home" className="w-8 h-8" />
            </button>
            <div className="w-full max-w-4xl mx-auto p-6 bg-white shadow-lg">
              <h2 className="text-3xl font-bold text-center mb-10">Your order is confirmed</h2>
              <div className="flex flex-col md:flex-row justify-between">
                <div className="mb-10 md:mb-0 md:mr-10" style={{ lineHeight: '1.6' }}>
                  <h3 className="text-xl font-semibold mb-4 text-center md:text-left">User Information</h3>
                  <p>First Name: <span>{firstName}</span></p>
                  <p>Last Name: <span>{lastName}</span></p>
                  <p>Email: <span>{email}</span></p>
                  <p>Phone Number: <span>{phoneNumber}</span></p>
                  <p>Address Line: <span>{addressLine}</span></p>
                  <p>City: <span>{city}</span></p>
                  <p>State: <span>{state}</span></p>
                  <p>Zip Code: <span>{zipCode}</span></p>
                  <p>Card ending with: <span>{cardNumber.slice(-4)}</span></p>
                </div>
                <div className="md:w-1/2">
                  <h3 className="text-xl font-semibold mb-4 text-center md:text-left">Ordered Items</h3>
                  {products.map((product, index) => (
                    isBuying[index] && (
                      <div key={index} className="flex justify-between items-center mb-4">
                        <div className="flex items-center">
                          <img src={product.image} alt={product.name} className="w-10 h-10 mr-2" />
                          <div>
                            <p>{product.name}</p>
                            <p>x{count[index]}</p>
                          </div>
                        </div>
                        <p>${(product.price * count[index]).toFixed(2)}</p>
                      </div>
                    )
                  ))}
                  <div className="border-t pt-4 mt-4 text-right">
                    <div className="flex justify-between items-center font-bold">
                      <p>Subtotal</p>
                      <p>${totalPrice.toFixed(2)}</p>
                    </div>
                    <div className="flex justify-between items-center font-bold">
                      <p>Total</p>
                      <p>${totalPrice.toFixed(2)}</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        );
      }
    }

export default MainPage;
