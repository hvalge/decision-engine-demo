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
      <label for="loanPeriod">Loan Period (Months):</label>
      <input v-model.number="loanApplication.loanPeriod" type="number" id="loanPeriod" />
    </div>
    
    <button @click="submitApplication">Submit Application</button>

    <h2>Decision</h2>
    <div v-if="decision">
      Decision: {{ decision.decision }}
      <div v-if="decision.decision === 'APPROVED'">
        Approved Amount: €{{ decision.approvedAmount }}
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
        loanPeriod: null,
      },
      decision: null
    };
  },
  methods: {
    submitApplication() {
      axios.post('http://localhost:8080/api/loan/decision', this.loanApplication)
        .then(response => {
          this.decision = response.data;
        })
        .catch(error => console.error(error));
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
</style>
