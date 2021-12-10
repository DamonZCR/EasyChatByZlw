package model

import (
	"errors"
)

//根据业务逻辑需要，自定义一些错误.
var (
	// 使用error.New()可以自定义错误内容。
	ERROR_USER_NOTEXISTS = errors.New("用户不存在..")
	ERROR_USER_EXISTS    = errors.New("用户已经存在...")
	ERROR_USER_PWD       = errors.New("密码不正确")
)
