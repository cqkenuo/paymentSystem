<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="账户号" prop="aAccount">
      <el-input v-model="dataForm.aAccount" placeholder="账户号"></el-input>
    </el-form-item>
    <el-form-item label="密码" prop="aPassword">
      <el-input v-model="dataForm.aPassword" placeholder="密码"></el-input>
    </el-form-item>
    <el-form-item label="账户余额" prop="aMoney">
      <el-input v-model="dataForm.aMoney" placeholder="账户余额"></el-input>
    </el-form-item>
    <el-form-item label="姓名" prop="aName">
      <el-input v-model="dataForm.aName" placeholder="姓名"></el-input>
    </el-form-item>
    <el-form-item label="年龄
" prop="aAge">
      <el-input v-model="dataForm.aAge" placeholder="年龄
"></el-input>
    </el-form-item>
    <el-form-item label="性别" prop="aSex">
      <el-input v-model="dataForm.aSex" placeholder="性别"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          aId: 0,
          aAccount: '',
          aPassword: '',
          aMoney: '',
          aName: '',
          aAge: '',
          aSex: ''
        },
        dataRule: {
          aAccount: [
            { required: true, message: '账户号不能为空', trigger: 'blur' }
          ],
          aPassword: [
            { required: true, message: '密码不能为空', trigger: 'blur' }
          ],
          aMoney: [
            { required: true, message: '账户余额不能为空', trigger: 'blur' }
          ],
          aName: [
            { required: true, message: '姓名不能为空', trigger: 'blur' }
          ],
          aAge: [
            { required: true, message: '年龄
不能为空', trigger: 'blur' }
          ],
          aSex: [
            { required: true, message: '性别不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.aId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.aId) {
            this.$http({
              url: this.$http.adornUrl(`/bank/account/info/${this.dataForm.aId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.aAccount = data.account.aAccount
                this.dataForm.aPassword = data.account.aPassword
                this.dataForm.aMoney = data.account.aMoney
                this.dataForm.aName = data.account.aName
                this.dataForm.aAge = data.account.aAge
                this.dataForm.aSex = data.account.aSex
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/bank/account/${!this.dataForm.aId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'aId': this.dataForm.aId || undefined,
                'aAccount': this.dataForm.aAccount,
                'aPassword': this.dataForm.aPassword,
                'aMoney': this.dataForm.aMoney,
                'aName': this.dataForm.aName,
                'aAge': this.dataForm.aAge,
                'aSex': this.dataForm.aSex
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
