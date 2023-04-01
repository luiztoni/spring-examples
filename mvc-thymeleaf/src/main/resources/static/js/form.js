function validatePassword() {
     var password = document.getElementById("password").value;
     var confirmPassword = document.getElementById("confirmPassword").value;
     var register = document.getElementById("register");
     if (password === confirmPassword && password.length >= 3) {
          register.disabled = false;
     } else {
          register.disabled = true;
     }
}

async function validateCpf() {
     var cpf = document.getElementById("cpf").value;
     try {
          let url = `/validate/cpf/${cpf}`;
          let response = await fetch(url);
          let text = await response.text();
          if (text === 'ok') {
             document.getElementById("invalidCpf").style.display = 'none';
          } else {
             document.getElementById("invalidCpf").style.display = '';
          }
      } catch (error) {
          console.log('register ' + error);
      }  
}
