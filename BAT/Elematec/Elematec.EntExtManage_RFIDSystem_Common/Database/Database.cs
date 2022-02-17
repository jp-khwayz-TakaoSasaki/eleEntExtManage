using System;
using System.Data;
using System.Data.SqlClient;
          
namespace Elematec.EntExtManage_RFIDSystem_Common.Database
{
    /// <summary>
    /// SQL Server処理関連
    /// </summary>
    public class Database : IDisposable
    {
        private SqlConnection _sqlConnection = null;

        private SqlTransaction _sqlTransaction = null;

        private SqlCommand _sqlCommand = null;

        private SqlDataReader _sqlDataReader = null;

        private bool _disposedValue = false;

        public SqlDataReader DataReader { get { return _sqlDataReader; } }

        /// <summary>
        /// コンストラクタ
        /// </summary>
        public Database(string ConnectionString)
        {
            _sqlConnection = new SqlConnection(ConnectionString);
            _sqlConnection.Open();

            _sqlCommand = new SqlCommand { Connection = _sqlConnection };
        }

        /// <summary>
        /// Dispose()
        /// </summary>
        public void Dispose()
        {
            Dispose(true);
            GC.SuppressFinalize(this);
        }

        /// <summary>
        /// Dispose
        /// </summary>
        /// <param name="disposing"></param>
        public virtual void Dispose(bool disposing)
        {
            if (_disposedValue)
            {
                return;
            }

            try
            {
                if (!disposing)
                {
                    return;
                }

                if (_sqlConnection == null)
                {
                    return;
                }

                if (_sqlCommand != null)
                {
                    if (_sqlDataReader != null)
                    {
                        if (!_sqlDataReader.IsClosed)
                        {
                            _sqlDataReader.Close();
                        }
                        _sqlDataReader.Dispose();
                        _sqlDataReader = null;
                    }

                    if (_sqlTransaction != null)
                    {
                        _sqlTransaction.Dispose();
                        _sqlTransaction = null;
                    }
                    _sqlCommand.Dispose();
                    _sqlCommand = null;
                }
                _sqlConnection.Close();
                _sqlConnection.Dispose();
                _sqlConnection = null;
            }
            finally
            {
                _disposedValue = true;
            }
        }

        #region [トランザクション]

        /// <summary>
        /// トランザクション開始
        /// </summary>
        public void BeginTransaction()
        {
            // 実行前にDataReadertはClose
            CloseSqlDataReader();
            _sqlTransaction = _sqlConnection.BeginTransaction(IsolationLevel.ReadCommitted);
            _sqlCommand.Transaction = _sqlTransaction;
        }

        /// <summary>
        /// コミット
        /// </summary>
        public void CommitTransaction()
        {
            _sqlTransaction.Commit();
            _sqlTransaction.Dispose();
            _sqlTransaction = null;
        }

        /// <summary>
        /// ロールバック
        /// </summary>
        public void RollbackTransaction()
        {
            _sqlTransaction.Rollback();
            _sqlTransaction.Dispose();
            _sqlTransaction = null;
        }

        #endregion

        #region [SQL処理]

        /// <summary>
        /// SQL設定
        /// </summary>
        /// <param name="sql">The SQL.</param>
        public void SetSql(string sql)
        {
            _sqlCommand.CommandText = sql;

            // SQLを設定した際はパラメーターをクリアする
            ClearParameter();
            // SQLを設定した際はCommandTypeを規定値(Text)に戻す
            _sqlCommand.CommandType = CommandType.Text;
            _sqlCommand.CommandTimeout = 180;
        }

        /// <summary>
        /// CommandTypeを設定します
        /// </summary>
        /// <param name="commandType"></param>
        public void SetSqlCommandType(CommandType commandType)
        {
            _sqlCommand.CommandType = commandType;
        }

        /// <summary>
        /// SQL実行(SELECT以外)
        /// </summary>
        /// <returns></returns>
        public int ExecuteNonQuery()
        {
            // 実行前にDataReadertはClose
            CloseSqlDataReader();
            //_sqlCommand.CommandTimeout = 180;
            return _sqlCommand.ExecuteNonQuery();
        }

        /// <summary>
        /// SELECT実行
        /// </summary>
        public void ExecuteQuery()
        {
            // 実行前にDataReadertはClose
            CloseSqlDataReader();
            //_sqlCommand.CommandTimeout = 180;
            _sqlDataReader = _sqlCommand.ExecuteReader();
        }

        /// <summary>
        /// DataReaderを閉じる
        /// </summary>
        private void CloseSqlDataReader()
        {
            if (_sqlDataReader != null && !_sqlDataReader.IsClosed)
            {
                _sqlDataReader.Close();
            }
        }

        #endregion

        #region [パラメーター]

        /// <summary>
        /// パラメータークリア
        /// </summary>
        public void ClearParameter()
        {
            _sqlCommand.Parameters.Clear();
        }

        /// <summary>
        /// パラメーター追加
        /// </summary>
        /// <param name="name">パラメーター名</param>
        /// <param name="parameter">パラメーター値</param>
        public void AddParameter(string name, object parameter)
        {
            _sqlCommand.Parameters.AddWithValue(name, parameter);
        }

        /// <summary>
        /// 出力パラメータを追加します　※サイズを指定する必要がある場合
        /// </summary>
        /// <param name="name">パラメータ名</param>
        /// <param name="dbType">タイプ</param>
        /// <param name="length">サイズ</param>
        public void AddOutParameter(string name, SqlDbType dbType, int length)
        {
            _sqlCommand.Parameters.Add(name, dbType, length);
        }

        /// <summary>
        /// 出力パラメータを追加します　※サイズを指定しないタイプ用
        /// </summary>
        /// <param name="name">パラメータ名</param>
        /// <param name="dbType">タイプ</param>
        public void AddOutParameter(string name, SqlDbType dbType)
        {
            _sqlCommand.Parameters.Add(name, dbType);
        }

        /// <summary>
        /// パラメータの型を追加します
        /// </summary>
        /// <param name="name">パラメータ名</param>
        /// <param name="direction">パラメータの型</param>
        public void AddDirection(string name, ParameterDirection direction)
        {
            _sqlCommand.Parameters[name].Direction = direction;
        }

        /// <summary>
        /// SQLCommnadオブジェクトを取得します
        /// </summary>
        /// <returns></returns>
        public SqlCommand GetSqlCommnd()
        {
            return _sqlCommand;
        }

        /// <summary>
        /// SQLCommandオブジェクトのCommandTimeoutを設定します
        /// </summary>
        /// <param name="value"></param>
        public void SetCommandtimeout(int value)
        {
            _sqlCommand.CommandTimeout = value;
        }

        #endregion

    }
}
