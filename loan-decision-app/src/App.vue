<template>
  <div id="app">
    <h1>Loan Application</h1>
    
    <div>
      <label for="personalCode">Personal Code:</label>
      <input v-model="loanApplication.personalCode" type="text" id="personalCode" />
    </div>
    
    <div>
      <label for="loanAmount">Loan Amount (€):</label>
      <input v-model.number="loanApplication.loanAmount" type="number" id="loanAmount" />
    </div>
    
    <div>
      <label for="loanPeriodInMonths">Loan Period (Months):</label>
      <input v-model.number="loanApplication.loanPeriodInMonths" type="number" id="loanPeriodInMonths" />
    </div>
    
    <button @click="submitApplication">Submit Application</button>

    <div v-if="validationErrors.length" class="error-messages">
      <p>Please correct the following errors:</p>
      <ul>
        <li v-for="error in validationErrors" :key="error">{{ error }}</li>
      </ul>
    </div>
    
    <h2>Decision</h2>
    <div v-if="decision">
      Decision: {{ decision.decision }}
      <div v-if="decision.decision === 'APPROVED'">
        Approved Amount: €{{ decision.approvedAmount }} <br/>
        Approved Period: {{ decision.approvedPeriod }} months
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'App',
  data() {
    return {
      loanApplication: {
        personalCode: '',
        loanAmount: null,
        loanPeriodInMonths: null,
      },
      decision: null,
      validationErrors: []
    };
  },
  methods: {
    validateInput() {
      this.validationErrors = [];

      if (!this.loanApplication.personalCode || this.loanApplication.personalCode.length !== 11 || !/^\d+$/.test(this.loanApplication.personalCode)) {
        this.validationErrors.push('Personal code must be exactly 11 digits long and only contain numbers.');
      }
      if (this.loanApplication.loanAmount < 2000 || this.loanApplication.loanAmount > 10000) {
        this.validationErrors.push('Loan Amount must be between €2,000 and €10,000.');
      }
      if (this.loanApplication.loanPeriodInMonths < 12 || this.loanApplication.loanPeriodInMonths > 60) {
        this.validationErrors.push('Loan Period must be between 12 and 60 months.');
      }
      return this.validationErrors.length === 0;
  },
  submitApplication() {
    if (!this.validateInput()) {
      return;
    }
    
    const applicationData = {
      ...this.loanApplication,
      loanAmount: this.loanApplication.loanAmount * 100
    };

    axios.post('http://localhost:8080/api/loan/decision', applicationData)
      .then(response => {
        this.decision = response.data;
      })
      .catch(error => {
        console.error(error);
        this.validationErrors.push('An error occurred while submitting the application.');
      });
    }
  }
};
</script>

<style>
#app {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

h1, h2 {
  text-align: center;
}

div {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

input[type="text"],
input[type="number"] {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  width: 100%;
  padding: 10px;
  border: none;
  background-color: #4CAF50;
  color: white;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

button:hover {
  background-color: #45a049;
}

button:disabled {
  background-color: #ccc;
  cursor: default;
}

.decision-output {
  background-color: #f2f2f2;
  padding: 15px;
  border-radius: 4px;
}

.decision-output > div {
  margin-top: 10px;
}

.error-messages {
  background-color: #ffdddd;
  border-left: 6px solid #f44336;
  margin: 0 0 15px 0;
  padding: 4px 12px;
}

.error-messages p {
  margin: 0;
}

.error-messages ul {
  margin: 10px 0 0 20px;
  padding: 0;
}

.error-messages li {
  list-style-type: disc;
}
</style>
