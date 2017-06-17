using DataAccessLayer.DbContext;
using DataAccessLayer.Helpers;
using DataAccessLayer.Interfaces;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Linq.Expressions;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Repository
{
    public class GenericRepository<T> : IGenericRepository<T> where T : class
    {
        private USR_DBEntities dbContext;
        private DbSet<T> dbSet;

        public GenericRepository(USR_DBEntities dbContext)
        {
            this.dbContext = dbContext;
            dbSet = dbContext.Set<T>();
        }

        public IEnumerable<T> GetAll()
        {
            return dbSet.AsEnumerable();
        }

        public T Get(object id)
        {
            return dbSet.Find(id);
        }

        public IEnumerable<T> Get(Expression<Func<T, Boolean>> predicate, Expression<Func<T, int>> orderBy, int skipCount, int takeCount, bool isAsc)
        {
            if (isAsc == true)
            {
                return dbSet.Where(predicate).OrderBy(orderBy).Skip(skipCount).Take(takeCount);
            }
            else
            {
                return dbSet.Where(predicate).OrderByDescending(orderBy).Skip(skipCount).Take(takeCount);
            }
        }

        public IEnumerable<T> Get(Expression<Func<T, Boolean>> predicate, Expression<Func<T, double>> orderBy, int skipCount, int takeCount, bool isAsc)
        {
            if (isAsc == true)
            {
                return dbSet.Where(predicate).OrderBy(orderBy).Skip(skipCount).Take(takeCount);
            }
            else
            {
                return dbSet.Where(predicate).OrderByDescending(orderBy).Skip(skipCount).Take(takeCount);
            }
        }
        public IEnumerable<T> Get(Expression<Func<T, Boolean>> predicate, Expression<Func<T, int>> orderBy, int skipCount, int takeCount)
        {
            return dbSet.Where(predicate).OrderBy(orderBy).Skip(skipCount).Take(takeCount);
        }
        public IEnumerable<T> Get(Expression<Func<T, Boolean>> predicate, Expression<Func<T, double>> orderBy, int skipCount, int takeCount)
        {
            return dbSet.Where(predicate).OrderBy(orderBy).Skip(skipCount).Take(takeCount);
        }

        public void Create(T entity)
        {
            dbSet.Add(entity);
        }

        public void Update(T entity)
        {
            //dbSet.
        }
        public IEnumerable<T> Find(Expression<Func<T, Boolean>> predicate)
        {
            return dbSet.Where(predicate).ToList();
        }
        public double Sum(Expression<Func<T, Boolean>> wherePredicate, Expression<Func<T, double>> sumPredicate)
        {
            try
            {
                return Math.Round(dbSet.Where(wherePredicate).Sum(sumPredicate), 2);
            }
            catch
            {
                return 0;
            }
        }
        public int Sum(Expression<Func<T, Boolean>> wherePredicate, Expression<Func<T, int>> sumPredicate)
        {
            try
            {
                return dbSet.Where(wherePredicate).Sum(sumPredicate);
            }
            catch
            {
                return 0;
            }
        }
        public double Average(Expression<Func<T, Boolean>> wherePredicate, Expression<Func<T, double>> averagePredicate)
        {
            try
            {
                return Math.Round(dbSet.Where(wherePredicate).Average(averagePredicate), 2);
            }
            catch
            {
                return 0;
            }
        }
        public double Average(Expression<Func<T, Boolean>> wherePredicate, Expression<Func<T, int>> averagePredicate)
        {
            try
            {
                return Math.Round(dbSet.Where(wherePredicate).Average(averagePredicate), 2);
            }
            catch
            {
                return 0;
            }
        }

        public double Max(Expression<Func<T, bool>> wherePredicate, Expression<Func<T, double>> maxPredicate)
        {
            try
            {
                return Math.Round(dbSet.Where(wherePredicate).Max(maxPredicate), 2);
            }
            catch
            {
                return 0;
            }
        }

        public int Max(Expression<Func<T, bool>> wherePredicate, Expression<Func<T, int>> maxPredicate)
        {
            try
            {
                return dbSet.Where(wherePredicate).Max(maxPredicate);
            }
            catch
            {
                return 0;
            }
        }

        public void Delete(object id)
        {
            T entity = dbSet.Find(id);
            if (entity != null)
            {
                dbSet.Remove(entity);
            }
        }

        public void Dispose()
        {
            dbContext.Dispose();
        }

    }
}