import { useState } from 'react';
import axios from 'axios';
import './App.css';

function App() {
  // Toggle between 'customer' and 'admin' view
  const [view, setView] = useState('customer'); 

  // Customer Data
  const [cardNumber, setCardNumber] = useState('');
  const [pin, setPin] = useState('');
  const [amount, setAmount] = useState(0);
  const [type, setType] = useState('withdraw');
  const [message, setMessage] = useState('');

  // Admin Data (List of transactions)
  const [transactions, setTransactions] = useState([]);

  // --- CUSTOMER FUNCTION: Send Transaction ---
  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("Processing...");
    try {
      // Talk to System 1 (Gateway)
      const response = await axios.post('http://localhost:8081/api/transaction', {
        cardNumber, pin, amount: Number(amount), type
      });
      setMessage(response.data);
    } catch (error) {
      setMessage("Error: Could not connect to Bank System.");
    }
  };

  // --- ADMIN FUNCTION: Get History ---
  const fetchTransactions = async () => {
    try {
      // Talk to System 2 (Core Bank) directly for logs
      const response = await axios.get('http://localhost:8082/process/logs');
      setTransactions(response.data);
    } catch (error) {
      alert("Could not load transactions. Is System 2 running?");
    }
  };

  return (
    <div style={{ padding: '20px', fontFamily: 'Arial', maxWidth: '800px', margin: '0 auto' }}>
      
      {/* HEADER WITH TOGGLE BUTTONS */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '30px' }}>
        <h1>🏦 PayTabs Bank POC</h1>
        <div>
            <button onClick={() => setView('customer')} style={{ marginRight: '10px', padding: '10px', cursor: 'pointer' }}>
                👤 Customer
            </button>
            <button onClick={() => { setView('admin'); fetchTransactions(); }} style={{ padding: '10px', cursor: 'pointer', background: '#333', color: 'white' }}>
                👮 Super Admin
            </button>
        </div>
      </div>

      {/* --- VIEW 1: CUSTOMER SCREEN --- */}
      {view === 'customer' && (
        <div style={{ border: '1px solid #ccc', padding: '30px', borderRadius: '10px' }}>
          <h2>Make a Transaction</h2>
          <form onSubmit={handleSubmit}>
            <div style={{ marginBottom: '15px' }}>
                <label>Card Number:</label>
                <input type="text" placeholder="4123..." onChange={e => setCardNumber(e.target.value)} style={{ width: '100%', padding: '8px' }} />
            </div>
            <div style={{ marginBottom: '15px' }}>
                <label>PIN:</label>
                <input type="password" placeholder="1234" onChange={e => setPin(e.target.value)} style={{ width: '100%', padding: '8px' }} />
            </div>
            <div style={{ marginBottom: '15px' }}>
                <label>Amount:</label>
                <input type="number" onChange={e => setAmount(e.target.value)} style={{ width: '100%', padding: '8px' }} />
            </div>
            <div style={{ marginBottom: '15px' }}>
                <label>Type:</label>
                <select onChange={e => setType(e.target.value)} style={{ width: '100%', padding: '8px' }}>
                    <option value="withdraw">Withdraw</option>
                    <option value="topup">Top Up</option>
                </select>
            </div>
            <button type="submit" style={{ background: 'green', color: 'white', padding: '10px 20px', border: 'none', cursor: 'pointer' }}>
                Submit
            </button>
          </form>
          <h3 style={{ color: 'blue', marginTop: '20px' }}>Result: {message}</h3>
        </div>
      )}

      {/* --- VIEW 2: ADMIN SCREEN --- */}
      {view === 'admin' && (
        <div>
          <h2>📊 Transaction Logs (System 2)</h2>
          <button onClick={fetchTransactions} style={{ marginBottom: '10px' }}>🔄 Refresh</button>
          
          <table border="1" cellPadding="10" style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
                <tr style={{ background: '#f4f4f4' }}>
                    <th>ID</th>
                    <th>Time</th>
                    <th>Card Number</th>
                    <th>Type</th>
                    <th>Amount</th>
                    <th>Status</th>
                    <th>Reason</th>
                </tr>
            </thead>
            <tbody>
                {transactions.map(t => (
                    <tr key={t.id}>
                        <td>{t.id}</td>
                        <td>{t.timestamp}</td>
                        <td>{t.cardNumber}</td>
                        <td>{t.type}</td>
                        <td>{t.amount}</td>
                        <td style={{ color: t.status === 'SUCCESS' ? 'green' : 'red' }}>
                            {t.status}
                        </td>
                        <td>{t.reason}</td>
                    </tr>
                ))}
            </tbody>
          </table>
        </div>
      )}

    </div>
  );
}

export default App;