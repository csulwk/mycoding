<template>
    <div>
        Username: <input type="text" v-model="loginForm.username" placeholder="username"/>
        <br><br>
        Password: <input type="password" v-model="loginForm.password" placeholder="password"/>
        <br><br>
        <button v-on:click="login">LOGIN</button>
    </div>
</template>

<script>
    export default {
        name: "Login",
        data () {
            return {
                loginForm: {
                    username: '',
                    password: ''
                }
            }
        },
        methods: {
            login () {
                this.$axios
                    .post('/login', {
                        username: this.loginForm.username,
                        password: this.loginForm.password
                    })
                    .then(resp => {
                        console.log(resp)
                        console.log(resp.data.retCode)
                        if (resp.data.retCode === '000000'){
                            this.$router.push('/')
                        }else {
                            this.$router.push('/register')
                        }
                    })
                    .catch(resp => {
                        console.log(resp)
                    })
            }
        }
    }
</script>

<style scoped>

</style>