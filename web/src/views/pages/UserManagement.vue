<template>
  <div class="user-management">
    <h2>用户管理</h2>
    <el-button type="primary" @click="fetchUsers" class="refresh-button">刷新用户列表</el-button>
    <el-button type="success" @click="openAddDialog" class="add-button">添加用户</el-button>
    <el-table :data="users" stripe style="width: 100%">
      <el-table-column label="序号" width="60" class-name="center-cell">
        <template #default="{ $index }">
          {{ $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="userName" label="用户名" width="200" class-name="center-cell"></el-table-column>
      <el-table-column prop="email" label="邮箱" width="200" class-name="center-cell"></el-table-column>
      <el-table-column prop="roleDisplay" label="角色" width="100" class-name="center-cell"></el-table-column>
      <el-table-column label="操作" width="200" class-name="center-cell">
        <template #default="{ row }">
          <el-button type="success" @click="openEditDialog(row)">修改</el-button>
          <el-button type="danger" @click="deleteUser(row.userId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 修改用户的弹窗 -->
    <el-dialog title="修改用户" v-model="isEditDialogVisible" width="500px">
      <el-form :model="currentUser" ref="editForm" label-width="80px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="currentUser.userName"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="currentUser.email"></el-input>
        </el-form-item>
      </el-form>
      <span class="dialog-footer">
        <el-button @click="isEditDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitEdit">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 添加用户的弹窗 -->
    <el-dialog title="添加用户" v-model="isAddDialogVisible" width="500px">
      <el-form :model="newUser" ref="addForm" label-width="80px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="newUser.userName"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="newUser.email"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="newUser.password" type="password"></el-input>
        </el-form-item>
      </el-form>
      <span class="dialog-footer">
        <el-button @click="isAddDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitAdd">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 删除确认弹窗 -->
    <el-dialog title="确认删除" v-model="isDeleteDialogVisible" width="400px">
      <p>确定要删除该用户吗？</p>
      <span class="dialog-footer">
        <el-button @click="isDeleteDialogVisible = false">取 消</el-button>
        <el-button type="danger" @click="confirmDelete">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import instance from "@/http";

export default {
  data() {
    return {
      users: [],
      isEditDialogVisible: false,
      isAddDialogVisible: false,
      isDeleteDialogVisible: false, // 删除确认弹窗
      currentUser: {
        userId: '',
        userName: '',
        email: '',
      },
      newUser: {
        userName: '',
        email: '',
        password: '',
      },
      userIdToDelete: '', // 要删除的用户 ID
    };
  },
  created() {
    this.fetchUsers();
  },
  methods: {
    async fetchUsers() {
      try {
        const response = await instance.get('/xblog/admin/user/all');
        if (response.data.success) {
          this.users = response.data.data.data;
        } else {
          this.$message.error('获取用户列表失败');
        }
      } catch (error) {
        console.error('请求用户列表失败:', error);
        this.$message.error('请求用户列表失败，请重试');
      }
    },
    openEditDialog(user) {
      this.currentUser = { ...user }; // 深拷贝
      this.isEditDialogVisible = true; // 打开弹窗
    },
    openAddDialog() {
      this.newUser = { userName: '', email: '', password: '' }; // 重置新用户信息
      this.isAddDialogVisible = true; // 打开添加用户弹窗
    },
    async submitEdit() {
      try {
        const response = await instance.post(`/xblog/admin/user/update`, this.currentUser);
        if (response.data.success) {
          this.$message.success('用户修改成功');
          this.isEditDialogVisible = false; // 关闭弹窗
          await this.fetchUsers(); // 刷新用户列表
        } else {
          this.$message.error('用户修改失败');
        }
      } catch (error) {
        console.error('修改用户失败:', error);
        this.$message.error('修改用户失败，请重试');
      }
    },
    async submitAdd() {
      try {
        const response = await instance.post(`/xblog/admin/user/add`, this.newUser);
        if (response.data.success) {
          this.$message.success('用户添加成功');
          this.isAddDialogVisible = false; // 关闭弹窗
          await this.fetchUsers(); // 刷新用户列表
        } else {
          this.$message.error('用户添加失败');
        }
      } catch (error) {
        console.error('添加用户失败:', error);
        this.$message.error('添加用户失败，请重试');
      }
    },
    deleteUser(userId) {
      this.userIdToDelete = userId; // 保存要删除的用户 ID
      this.isDeleteDialogVisible = true; // 打开删除确认弹窗
    },
    async confirmDelete() {
      try {
        const response = await instance.post(`/xblog/admin/user/del`, {
          userId: this.userIdToDelete,
        });
        if (response.data.success) {
          this.$message.success('用户删除成功');
          await this.fetchUsers(); // 刷新用户列表
        } else {
          this.$message.error('用户删除失败');
        }
      } catch (error) {
        console.error('删除用户失败:', error);
        this.$message.error('删除用户失败，请重试');
      } finally {
        this.isDeleteDialogVisible = false; // 关闭弹窗
        this.userIdToDelete = ''; // 重置用户 ID
      }
    },
  },
};
</script>

<style scoped>
.user-management {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  color: #333;
}

.refresh-button {
  margin-bottom: 20px; /* 增加按钮与表格之间的间距 */
}

.add-button {
  margin-bottom: 20px; /* 增加添加按钮与表格之间的间距 */
}

.el-table {
  margin-top: 20px; /* 表格与标题之间的间距 */
}

.el-button {
  margin-right: 10px; /* 按钮之间的间距 */
}

/* 居中显示样式 */
.center-header {
  text-align: center; /* 表头居中 */
}

.center-cell {
  text-align: center; /* 单元格内容居中 */
}
</style>
