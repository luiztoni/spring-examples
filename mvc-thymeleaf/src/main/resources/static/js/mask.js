function loadMask(obj, mask) {
    this.obj=obj;
    this.mask=mask;
    setTimeout("execMask()", 1);
}

function execMask() {
    this.obj.value=this.mask(obj.value);
}

function phoneMask(phone) {
    phone=phone.replace(/\D/g,"");
    phone=phone.replace(/^(\d)/,"($1");
    phone=phone.replace(/(.{3})(\d)/,"$1)$2");
    if(phone.length == 9) {
        phone=phone.replace(/(.{1})$/,"-$1");
    } else if (tel.length == 10) {
        phone=phone.replace(/(.{2})$/,"-$1");
    } else if (tel.length == 11) {
        phone=phone.replace(/(.{3})$/,"-$1");
    } else if (tel.length == 12) {
        phone=phone.replace(/(.{4})$/,"-$1");
    } else if (tel.length > 12) {
        phone=phone.replace(/(.{4})$/,"-$1");
    }
    return phone;
}

function cpfMask(cpf) {
    cpf=cpf.replace(/\D/g,"");
    cpf=cpf.replace(/(\d{3})(\d)/,"$1.$2");
    cpf=cpf.replace(/(\d{3})(\d)/,"$1.$2");
    cpf=cpf.replace(/(\d{3})(\d{1,2})$/,"$1-$2");
    return cpf;
}